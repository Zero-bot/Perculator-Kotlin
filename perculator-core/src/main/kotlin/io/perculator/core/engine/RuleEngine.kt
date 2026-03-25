package io.perculator.core.engine

import io.perculator.core.evaluation.ConditionEvaluator
import io.perculator.core.evaluation.CookieEvaluator
import io.perculator.core.evaluation.HeaderEvaluator
import io.perculator.core.evaluation.ParameterEvaluator
import io.perculator.core.evaluation.RequestEvaluator
import io.perculator.core.model.Action
import io.perculator.core.model.Condition
import io.perculator.core.model.Location
import io.perculator.core.model.Rule
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache
import org.slf4j.LoggerFactory

public class RuleEngine(
    private val evaluators: Map<Location, ConditionEvaluator> = defaultEvaluators(),
) {
    private val logger = LoggerFactory.getLogger(RuleEngine::class.java)

    public fun evaluate(request: PerculatorHttpRequest, rules: List<Rule>): RuleEvaluationResult {
        val sortedRules = rules.sortedBy { it.priority }
        val matchedRules = mutableListOf<String>()
        var resultAction = Action.ALLOW
        var resultRuleName: String? = null

        for (rule in sortedRules) {
            val action = evaluateRule(rule, request)
            matchedRules.add(rule.name)
            logger.debug("Rule '{}' evaluated to action: {}", rule.name, action)

            if (action.priority <= resultAction.priority) {
                resultAction = action
                resultRuleName = rule.name
            }

            if (action == Action.REJECT) {
                logger.debug("Short-circuiting on REJECT from rule '{}'", rule.name)
                return RuleEvaluationResult(
                    action = Action.REJECT,
                    ruleName = rule.name,
                    matchedRules = matchedRules,
                )
            }
        }

        return RuleEvaluationResult(
            action = resultAction,
            ruleName = resultRuleName,
            matchedRules = matchedRules,
        )
    }

    private fun evaluateRule(rule: Rule, request: PerculatorHttpRequest): Action {
        if (rule.contextConditions.isNotEmpty()) {
            val contextMatch = rule.contextConditions.all { evaluateCondition(it, request) }
            if (!contextMatch) {
                logger.debug("Context conditions not met for rule '{}', skipping", rule.name)
                return Action.SKIP
            }
        }

        val conditionsMatch = rule.conditions.all { evaluateCondition(it, request) }
        return if (conditionsMatch) rule.actionOnMatch else rule.actionOnMismatch
    }

    private fun evaluateCondition(condition: Condition, request: PerculatorHttpRequest): Boolean {
        val evaluator = evaluators[condition.location]
            ?: throw IllegalStateException("No evaluator registered for location: ${condition.location}")
        return evaluator.evaluate(condition, request)
    }

    public companion object {
        public fun defaultEvaluators(regexCache: RegexCache = RegexCache()): Map<Location, ConditionEvaluator> = mapOf(
            Location.Parameter to ParameterEvaluator(regexCache),
            Location.Cookie to CookieEvaluator(regexCache),
            Location.Header to HeaderEvaluator(regexCache),
            Location.Request to RequestEvaluator(regexCache),
        )
    }
}
