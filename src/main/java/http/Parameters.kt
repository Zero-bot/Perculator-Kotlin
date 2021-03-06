package http

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.InvalidJsonException
import exception.KeyCannotBeNullException
import exception.NoSuchParameterException
import exception.OperatorNotSupportedException
import exception.ValueCannotBeNullException
import wrappers.MutableHttpServletRequest


class Parameters(val httpServletRequest: MutableHttpServletRequest) {

    companion object {
        const val hasParameter: Byte = 1
        const val matchParameter: Byte = 2
        const val hasParameterWithValue: Byte = 3
        const val matchParameterWithValue: Byte = 4
        const val match: Byte = 5
        const val countParameters: Byte = 6
        const val valueHasOnlyAlphaNumericChar: Byte = 7
    }
    private var jsonBody: Any? = null
    get() = field

    init {
        if (httpServletRequest.isJsonBody())
            jsonBody = parseJsonBody()
    }

    private val parameters: Map<String, Any>
        get() {
            return httpServletRequest.parameterMap as Map<String, String>
        }


    private fun MutableHttpServletRequest.isJsonBody(): Boolean {
        return httpServletRequest.hasContentType() && this.contentType.contains("json", true)
    }

    private fun MutableHttpServletRequest.hasContentType(): Boolean {
        return httpServletRequest.headerNames.toList().any{
                val headerName = it as String
                headerName.equals("content-type", true)
            }
    }

    private fun parseJsonBody(): Any? {
        return try {
            println(httpServletRequest.getBody())
            Configuration.defaultConfiguration().jsonProvider().parse(httpServletRequest.getBody())
        } catch (e: InvalidJsonException){
            null
        }
    }

    private fun hasParameter(key: String) = parameters.containsKey(key)

    private fun matchParameter(keyRegex: Regex) = parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.isNotEmpty()

    private fun hasParameterWithValue(key: String, value: String):Boolean{
        if(hasParameter(key).not()) throw NoSuchParameterException(key)
        return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, value) }
    }

    private fun matchParameterWithValue(keyRegex: Regex, valueRegex: Regex): Boolean {
        if(matchParameter(keyRegex).not()) throw NoSuchParameterException(keyRegex.toString())
        return parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.all { allMatch(it.value as Array<Any>, valueRegex) }

    }

    private fun match(key: String, regex: Regex): Boolean {
        if(hasParameter(key).not()) throw NoSuchParameterException(key)
        return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, regex) }
    }

    private fun countParameters(count: Int) = parameters.size == count

    private fun valueHasOnlyAlphaNumericChar(key: String): Boolean{
        if(hasParameter(key).not()) throw NoSuchParameterException(key)
        return parameters.filterKeys { it == key }.all { allMatch( it.value as Array<Any>, "^[a-zA-Z0-9]+$".toRegex() ) }
    }

    private fun allMatch(value: Array<Any>, regex: Regex): Boolean {
        return value.all { regex.containsMatchIn(it as CharSequence) }
    }

    private fun allMatch(value: Array<Any>, match: String): Boolean {
        return value.all { match == (it as CharSequence) }
    }

    fun evaluate(operator: Byte, key: String?, value: String?): Boolean {
        if (key != null) {
            if (operator == hasParameter) {
                return hasParameter(key)
            }

            if (operator == matchParameter) {
                return matchParameter(key.toRegex())
            }

            if (operator == hasParameterWithValue) {
                if (value == null) throw ValueCannotBeNullException("Parameter.hasValue")
                return hasParameterWithValue(key, value)
            }

            if (operator == matchParameterWithValue) {
                if (value == null) throw ValueCannotBeNullException("Parameter.matchValue")
                return matchParameterWithValue(key.toRegex(), value.toRegex())
            }

            if (operator == match) {
                if (value == null) throw ValueCannotBeNullException("Parameter.Match")
                return match(key, value.toRegex())
            }

            if (operator == countParameters) {
                val countConverted = key.toIntOrNull() ?: throw NumberFormatException()
                return countParameters(countConverted)
            }

            if (operator == valueHasOnlyAlphaNumericChar) {
                return valueHasOnlyAlphaNumericChar(key)
            }

            throw OperatorNotSupportedException(operator, "Parameter")
        }
        throw KeyCannotBeNullException("Parameter conditions")
    }

    fun evaluateJson(operator: Byte, key: String?, value: String?){

    }
}