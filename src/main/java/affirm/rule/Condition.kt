package affirm.rule

import exception.InvalidLocationException
import http.Cookies
import http.Headers
import http.Http
import http.Parameters
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
            Http.Cookie -> evaluateCookieCondition(httpServletRequest)
            Http.Header -> evaluateHeaderCondition(httpServletRequest)
            else -> throw InvalidLocationException(this.location)
        }
    }

    private fun evaluateParameterCondition(httpServletRequest: HttpServletRequest): Boolean {
        return Parameters(httpServletRequest).evaluate(this.operator, this.key, this.value)
    }

    private fun evaluateCookieCondition(httpServletRequest: HttpServletRequest): Boolean {
        return Cookies(httpServletRequest).evaluate(this.operator, this.key, this.value)
    }

    private fun evaluateHeaderCondition(httpServletRequest: HttpServletRequest): Boolean {
        return Headers(httpServletRequest).evaluate(this.operator, this.key, this.value)
    }
}