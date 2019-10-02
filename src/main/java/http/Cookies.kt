package http

import exception.OperatorNotSupportedException
import exception.ValueCannotBeNullException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

class Cookies(httpServletRequest: HttpServletRequest) {
    private val parameters: Array<Cookie> = httpServletRequest.cookies

    companion object{
        const val hasKey: Byte = 0
        const val hasValue: Byte = 1
        const val matchKey: Byte = 2
        const val matchValue: Byte = 3
        const val count: Byte = 4
        const val hasOnlyAlphaNumericChar: Byte = 5
        const val valueLengthEquals: Byte = 6
    }

    private fun hasKey(key: String) = this.parameters.any { it.name == key }

    private fun hasValue(key: String, value: String) = this.parameters.any { it.name == key && it.value == value }

    private fun matchKey(keyRegex: Regex) = this.parameters.any { keyRegex.containsMatchIn(it.name) }

    private fun matchValue(key: String, valueRegex: Regex) = this.parameters.any { it.name == key && valueRegex.containsMatchIn(it.value) }

    private fun count(count: Int) = this.parameters.size == count

    private fun hasOnlyAlphaNumericChar(key: String) = this.parameters.any { it.name == key && "^[a-zA-Z0-9]+$".toRegex().containsMatchIn(it.value) }

    private fun valueLengthEquals(key: String, length: Int) = this.parameters.any { it.name == key && it.value.length == length }

    fun evaluate(operator: Byte, key: String, value: String?): Boolean {
        if(operator == hasKey) return hasKey(key)

        if(operator == matchKey) return matchKey(key.toRegex())

        if(operator == hasValue) {
            if(value == null) throw ValueCannotBeNullException("hasValue")
            return hasValue(key, value)
        }

        if(operator == matchValue){
            if(value == null) throw ValueCannotBeNullException("matchValue")
            return matchValue(key, value.toRegex())
        }

        if(operator == count) return count(key.toInt())

        if(operator == hasOnlyAlphaNumericChar) return hasOnlyAlphaNumericChar(key)

        if(operator == valueLengthEquals){
            if(value == null) throw ValueCannotBeNullException("valueLengthEquals")
            return valueLengthEquals(key, value.toInt())
        }

        else throw OperatorNotSupportedException(operator, "Cookie")
    }
}