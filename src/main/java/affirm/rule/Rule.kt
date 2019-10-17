package affirm.rule

import result.Action
import javax.servlet.http.HttpServletRequest

class Rule(private val contextConditions: List<Condition>?, private val conditions: List<Condition>, private val actionSuccess: Action, private val actionFailure: Action, private val priority: Int = Int.MAX_VALUE) {

    fun getPriority() = this.priority

    private fun evaluateConditions(httpServletRequest: HttpServletRequest): Boolean {
        return conditions.all { condition ->
            condition.evaluate(httpServletRequest)
        }
    }

    private fun evaluateContextConditions(httpServletRequest: HttpServletRequest): Boolean{
        if(contextConditions.isNullOrEmpty()) return true

        return contextConditions.all{condition ->
            condition.evaluate(httpServletRequest)
        }
    }

    fun evaluate(httpServletRequest: HttpServletRequest): Action {

        if(evaluateContextConditions(httpServletRequest).not()) return Action.Skip

        return when(evaluateConditions(httpServletRequest)){
            true -> actionSuccess
            false -> actionFailure
        }
    }
}