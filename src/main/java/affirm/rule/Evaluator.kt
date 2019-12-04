package affirm.rule

import exception.InvalidLocationException
import http.*
import http.wrapper.RequestWrapper
import javax.servlet.http.HttpServletRequest

class Evaluator {
     private var condition: Condition

    constructor(condition: Condition){
        this.condition = condition
    }

    fun evaluateCondition(httpServletRequest: RequestWrapper): Boolean {
        if(condition.location == Http.Parameter){
            return Parameters(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }

        if(condition.location == Http.Cookie){
            return Cookies(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }

        if(condition.location == Http.Header){
            return Headers(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }

        if(condition.location == Http.Request){
            return Request(httpServletRequest).evaluate(condition.operator, condition.key)
        }
        else throw InvalidLocationException(condition.location)
    }

}