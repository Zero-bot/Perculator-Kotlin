package affirm

import affirm.rule.Rule
import result.Action
import wrappers.MutableHttpServletRequest
import javax.servlet.http.HttpServletRequest

class AffirmationBuilderFactory(val httpServletRequest: MutableHttpServletRequest, private val rules: Map<String, Rule>) {

    fun affirm(): Action {
        val actions = mutableListOf<Action>()
        for(rule in rules.keys){
            val action = (rules[rule] ?: error("rules map returned null for key $rule")).evaluate(httpServletRequest)
            actions.add(action)
            if(action == Action.Reject) return makeDecision(actions)
        }
        return makeDecision(actions)
    }
    private fun makeDecision(actions: List<Action>) = actions.minBy { it.ordinal }!!
}