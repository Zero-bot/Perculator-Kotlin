package main.affirm.rule

import main.exception.InvalidLocationException
import main.http.Http
import main.http.Parameters
import javax.servlet.http.HttpServletRequest

class Condition {
    var location: Byte = -1
    var operator: Byte = -1
    var key: String
    var value: String?

    constructor(location: Byte, operator: Byte, key: String, value: String){
        this.location = location
        this.operator = operator
        this.key = key
        this.value = value
    }

    constructor(location: Byte, operator: Byte, key: String){
        this.location = location
        this.operator = operator
        this.key = key
        this.value = null
    }

    fun evaluate(httpServletRequest: HttpServletRequest): Boolean {
        return when(this.location){
            Http.Parameter -> evaluateParameterCondition(httpServletRequest)
            else -> throw InvalidLocationException(this.location)
        }
    }

    private fun evaluateParameterCondition(httpServletRequest: HttpServletRequest): Boolean {
        return Parameters(httpServletRequest).evaluate(this.operator, this.key, this.value)
    }
}