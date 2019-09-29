package main.affirm.rule

import main.result.Action
import javax.servlet.http.HttpServletRequest

class Rule(private val conditions: Array<Condition>, private val actionSuccess: Action, private val actionFailure: Action) {
    private fun evaluateConditions(httpServletRequest: HttpServletRequest): Boolean {
        return conditions.all { condition ->
            condition.evaluate(httpServletRequest)
        }
    }

    fun evaluate(httpServletRequest: HttpServletRequest): Action {
        return when(evaluateConditions(httpServletRequest)){
            true -> actionSuccess
            false -> actionFailure
        }
    }
}