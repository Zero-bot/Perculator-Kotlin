package http

import exception.KeyCannotBeNullException
import exception.NoSuchCookieException
import exception.OperatorNotSupportedException
import exception.ValueCannotBeNullException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

class Cookies(httpServletRequest: HttpServletRequest) {
    private val parameters: Array<Cookie> = httpServletRequest.cookies

    companion object{
        const val hasCookie: Byte = 0
        const val cookieHasValue: Byte = 1
        const val matchCookie: Byte = 2
        const val matchValue: Byte = 3
        const val countCookies: Byte = 4
        const val valueHasOnlyAlphaNumericChar: Byte = 5
        const val valueLengthEquals: Byte = 6
    }

    private fun hasCookie(key: String) = this.parameters.any { it.name == key }

    private fun getKeyIndex(key: String): Int {
        for((index, value) in parameters.withIndex()){
            if(value.name == key)
                return index
        }
        return -1
    }

    private fun cookieHasValue(key: String, value: String): Boolean {
        val index = getKeyIndex(key)
        if(index == -1) throw NoSuchCookieException(key)
        return parameters[index].value == value
    }

    private fun matchCookie(keyRegex: Regex) = this.parameters.any { keyRegex.containsMatchIn(it.name) }

    private fun matchValue(key: String, valueRegex: Regex): Boolean {
        val index = getKeyIndex(key)
        if(index == -1) throw NoSuchCookieException(key)
       return valueRegex.containsMatchIn(parameters[index].value)
    }

    private fun countCookies(count: Int) = this.parameters.size == count

    private fun valueHasOnlyAlphaNumericChar(key: String): Boolean {
        val index = getKeyIndex(key)
        if(index == -1) throw NoSuchCookieException(key)
        return "^[a-zA-Z0-9]+$".toRegex().containsMatchIn(parameters[index].value)
    }

    private fun valueLengthEquals(key: String, length: Int): Boolean {
        val index = getKeyIndex(key)
        if(index == -1) throw NoSuchCookieException(key)
        return parameters[index].value.length == length
    }

    fun evaluate(operator: Byte, key: String?, value: String?): Boolean {
        if (key != null) {
            if (operator == hasCookie) return hasCookie(key)

            if (operator == matchCookie) return matchCookie(key.toRegex())

            if (operator == cookieHasValue) {
                if (value == null) throw ValueCannotBeNullException("hasValue")
                return cookieHasValue(key, value)
            }

            if (operator == matchValue) {
                if (value == null) throw ValueCannotBeNullException("matchValue")
                return matchValue(key, value.toRegex())
            }

            if (operator == countCookies) return countCookies(key.toInt())

            if (operator == valueHasOnlyAlphaNumericChar) return valueHasOnlyAlphaNumericChar(key)

            if (operator == valueLengthEquals) {
                if (value == null) throw ValueCannotBeNullException("valueLengthEquals")
                return valueLengthEquals(key, value.toInt())
            } else throw OperatorNotSupportedException(operator, "Cookie")
        }
        else throw KeyCannotBeNullException("Cookie conditions")
    }
}