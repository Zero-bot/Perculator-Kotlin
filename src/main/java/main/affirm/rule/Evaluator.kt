package main.affirm.rule

import main.affirm.rule.Condition
import main.exception.InvalidLocationException
import main.http.Http
import main.http.Parameters
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
        else throw InvalidLocationException(condition.location)
    }



}