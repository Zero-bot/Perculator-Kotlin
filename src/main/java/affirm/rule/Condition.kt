package affirm.rule

import exception.InvalidLocationException
import exception.KeyCannotBeNullException
import http.*
import kotlinx.serialization.Serializable
import wrappers.MutableHttpServletRequest
import javax.servlet.http.HttpServletRequest

@Serializable
class Condition {
    var location: Byte = -1
    var operator: Byte = -1
    val key: String?
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

    constructor(location: Byte, operator: Byte){
        this.location = location
        this.operator = operator
        this.key = null
        this.value = null
    }

    fun evaluate(httpServletRequest: MutableHttpServletRequest): Boolean {
        return when(this.location){
            Http.Parameter -> evaluateParameterCondition(httpServletRequest)
            Http.Cookie -> evaluateCookieCondition(httpServletRequest)
            Http.Header -> evaluateHeaderCondition(httpServletRequest)
            Http.Request -> evaluateRequestCondition(httpServletRequest)
            else -> throw InvalidLocationException(this.location)
        }
    }

    private fun evaluateParameterCondition(httpServletRequest: MutableHttpServletRequest): Boolean {
        if(this.key != null)
            return Parameters(httpServletRequest).evaluate(this.operator, this.key, this.value)
        throw KeyCannotBeNullException("Parameter conditions")
    }

    private fun evaluateCookieCondition(httpServletRequest: MutableHttpServletRequest): Boolean {
        if(this.key != null)
            return Cookies(httpServletRequest).evaluate(this.operator, this.key, this.value)
        throw KeyCannotBeNullException("Cookie conditions")
    }

    private fun evaluateHeaderCondition(httpServletRequest: MutableHttpServletRequest): Boolean {
        if(this.key != null)
            return Headers(httpServletRequest).evaluate(this.operator, this.key, this.value)
        throw KeyCannotBeNullException("Header conditions")
    }

    private fun evaluateRequestCondition(httpServletRequest: MutableHttpServletRequest): Boolean {
        return Request(httpServletRequest).evaluate(this.operator, this.key)
    }
}