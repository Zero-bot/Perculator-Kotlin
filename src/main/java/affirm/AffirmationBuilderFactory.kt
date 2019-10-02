package affirm

import affirm.rule.Rule
import result.Action
import javax.servlet.http.HttpServletRequest

class AffirmationBuilderFactory(private val httpServletRequest: HttpServletRequest, private val rules: Array<Rule>) {

    fun affirm(): Action {
        var actions = mutableListOf<Action>()
        for(rule in rules){
            var action = rule.evaluate(httpServletRequest)
            actions.add(action)
            if(action == Action.Reject) return makeDecision(actions)
        }
        return makeDecision(actions)
    }
    private fun makeDecision(actions: List<Action>) = actions.minBy { it.ordinal }!!
}