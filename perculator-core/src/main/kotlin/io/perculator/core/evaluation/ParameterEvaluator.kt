package io.perculator.core.evaluation

import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Operator.ParameterOp
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache

public class ParameterEvaluator(private val regexCache: RegexCache = RegexCache()) : ConditionEvaluator {

    override fun evaluate(condition: Condition, request: PerculatorHttpRequest): Boolean {
        val op = condition.operator as ParameterOp
        val key = condition.key
        val value = condition.value
        val params = request.parameterMap

        return when (op) {
            is ParameterOp.HasParameter -> {
                requireKey(key, "hasParameter")
                params.containsKey(key)
            }
            is ParameterOp.MatchParameter -> {
                requireKey(key, "matchParameter")
                val regex = regexCache.get(key!!)
                params.keys.any { regex.containsMatchIn(it) }
            }
            is ParameterOp.HasParameterWithValue -> {
                requireKey(key, "hasParameterWithValue")
                requireValue(value, "hasParameterWithValue")
                val values = params[key] ?: throw EvaluationException("No such parameter: $key")
                values.all { it == value }
            }
            is ParameterOp.MatchParameterWithValue -> {
                requireKey(key, "matchParameterWithValue")
                requireValue(value, "matchParameterWithValue")
                val keyRegex = regexCache.get(key!!)
                val valueRegex = regexCache.get(value!!)
                val matchingEntries = params.filterKeys { keyRegex.containsMatchIn(it) }
                if (matchingEntries.isEmpty()) throw EvaluationException("No parameter matching: $key")
                matchingEntries.values.all { arr -> arr.all { valueRegex.containsMatchIn(it) } }
            }
            is ParameterOp.Match -> {
                requireKey(key, "match")
                requireValue(value, "match")
                val values = params[key] ?: throw EvaluationException("No such parameter: $key")
                val regex = regexCache.get(value!!)
                values.all { regex.containsMatchIn(it) }
            }
            is ParameterOp.CountParameters -> {
                requireKey(key, "countParameters")
                val count = key!!.toIntOrNull() ?: throw EvaluationException("Invalid count: $key")
                params.size == count
            }
            is ParameterOp.ValueHasOnlyAlphaNumericChar -> {
                requireKey(key, "valueHasOnlyAlphaNumericChar")
                val values = params[key] ?: throw EvaluationException("No such parameter: $key")
                val regex = regexCache.get("^[a-zA-Z0-9]+$")
                values.all { regex.containsMatchIn(it) }
            }
        }
    }

    private fun requireKey(key: String?, operatorName: String) {
        if (key == null) throw EvaluationException("Key cannot be null for $operatorName")
    }

    private fun requireValue(value: String?, operatorName: String) {
        if (value == null) throw EvaluationException("Value cannot be null for $operatorName")
    }
}
