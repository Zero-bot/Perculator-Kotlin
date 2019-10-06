package affirm.rule

import exception.InvalidLocationException
import http.Cookies
import http.Headers
import http.Http
import http.Parameters
import javax.servlet.http.HttpServletRequest

class Evaluator {
     private var condition: Condition

    constructor(condition: Condition){
        this.condition = condition
    }

    fun evaluateCondition(httpServletRequest: HttpServletRequest): Boolean {
        if(condition.location == Http.Parameter){
            return Parameters(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }

        if(condition.location == Http.Cookie){
            return Cookies(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }

        if(condition.location == Http.Header){
            return Headers(httpServletRequest).evaluate(condition.operator, condition.key, condition.value)
        }
        else throw InvalidLocationException(condition.location)
    }

}