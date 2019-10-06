package http

import exception.OperatorNotSupportedException
import exception.ValueCannotBeNullException
import java.lang.NumberFormatException
import javax.servlet.http.HttpServletRequest

class Parameters(val httpServletRequest: HttpServletRequest) {

    companion object {
        const val hasParameter: Byte = 1
        const val matchParameter: Byte = 2
        const val hasParameterWithValue: Byte = 3
        const val matchParameterWithValue: Byte = 4
        const val match: Byte = 5
        const val countParameters: Byte = 6
        const val valueHasOnlyAlphaNumericChar: Byte = 7
    }

    private val parameters: Map<*, *>
        get() = this.httpServletRequest.parameterMap as Map<*, *>

    private fun hasParameter(key: String) = parameters.containsKey(key)

    private fun matchParameter(keyRegex: Regex) = parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.isNotEmpty()

    private fun hasParameterWithValue(key: String, value: String):Boolean{
        if(hasParameter(key))
            return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, value) }
        return false
    }

    private fun matchParameterWithValue(keyRegex: Regex, valueRegex: Regex): Boolean {
        if (matchParameter(keyRegex))
            return parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.all { allMatch(it.value as Array<Any>, valueRegex) }
        return false
    }

    private fun match(key: String, regex: Regex): Boolean {
        if(hasParameter(key))
            return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, regex) }
        return false
    }

    private fun countParameters(count: Int) = parameters.size == count

    private fun valueHasOnlyAlphaNumericChar(key: String): Boolean{
        if(hasParameter(key))
            return parameters.filterKeys { it == key }.all { allMatch( it.value as Array<Any>, "^[a-zA-Z0-9]+$".toRegex() ) }
        return false
    }

    private fun allMatch(value: Array<Any>, regex: Regex): Boolean {
        return value.all { regex.containsMatchIn(it as CharSequence) }
    }

    private fun allMatch(value: Array<Any>, match: String): Boolean {
        return value.all { match == (it as CharSequence) }
    }

    fun evaluate(operator: Byte, key: String, value: String?): Boolean {
        if(operator == hasParameter) {
            return hasParameter(key)
        }

        if(operator == matchParameter) {
            return matchParameter(key.toRegex())
        }

        if(operator == hasParameterWithValue){
            if (value == null) throw ValueCannotBeNullException("Parameter.hasValue")
            return hasParameterWithValue(key, value)
        }

        if(operator == matchParameterWithValue){
            if (value == null) throw ValueCannotBeNullException("Parameter.matchValue")
            return matchParameterWithValue(key.toRegex(), value.toRegex())
        }

        if(operator == match){
            if (value == null) throw ValueCannotBeNullException("Parameter.Match")
            return  match(key, value.toRegex())
        }

        if(operator == countParameters){
            val countConverted = key.toIntOrNull() ?: throw NumberFormatException()
            return countParameters(countConverted)
        }

        if(operator == valueHasOnlyAlphaNumericChar){
            return valueHasOnlyAlphaNumericChar(key)
        }

        throw OperatorNotSupportedException(operator, "Parameter")
    }
}