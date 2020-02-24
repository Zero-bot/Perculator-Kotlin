package http

import exception.KeyCannotBeNullException
import exception.NoSuchHeaderException
import exception.OperatorNotSupportedException
import exception.ValueCannotBeNullException
import java.lang.NumberFormatException
import javax.servlet.http.HttpServletRequest

class Headers(val httpServletRequest: HttpServletRequest) {

    companion object{
        const val hasHeader:Byte = 0
        const val headerHasValue:Byte = 1
        const val matchHeader:Byte = 2
        const val matchHeaderValue:Byte = 3
        const val isHeaderRepeated:Byte = 4
        const val headerHasSpecialChar:Byte = 5
        const val totalHeadersCountEquals:Byte = 6
        const val headerCountEquals:Byte = 7
        const val matchAllHeadersValue:Byte = 8
    }

    private fun hasHeader(key: String): Boolean {
        println(httpServletRequest.headerNames.toList())
        for (header in httpServletRequest.headerNames){
            println(header)
            if(key.equals(header as String, true))
                return true
        }
        return false
    }

    private fun headerHasValue(key: String, value: String): Boolean {
        if(hasHeader(key).not()) throw NoSuchHeaderException(key)
        return httpServletRequest.getHeaders(key).asSequence().any { it == value }
    }

        private fun matchHeader(keyRegex: Regex) = httpServletRequest.headerNames.toList().any {
            keyRegex.containsMatchIn(it as CharSequence)
        }

        private fun matchHeaderValue(key: String, valueRegex: Regex): Boolean {
            if(hasHeader(key).not()) throw NoSuchHeaderException(key)
            return httpServletRequest.getHeaders(key).asSequence().any { valueRegex.containsMatchIn(it.toString()) }
        }

        private fun matchAllHeadersValue(key: String, valueRegex: Regex): Boolean {
            if(hasHeader(key).not()) throw NoSuchHeaderException(key)
            return httpServletRequest.getHeaders(key).asSequence().all { valueRegex.containsMatchIn(it.toString()) }
        }

        private fun isHeaderRepeated(key: String) = httpServletRequest.getHeaders(key).toList().size > 1

        private fun headerHasSpecialChar(key: String): Boolean {
            if(hasHeader(key).not()) throw NoSuchHeaderException(key)
            return httpServletRequest.getHeaders(key).asSequence().any { "[\\W\\s_]".toRegex().containsMatchIn(it.toString()) }
        }

        private fun totalHeadersCountEquals(count: Int): Boolean {
            var headerCount = 0
            for (key in httpServletRequest.headerNames){
                headerCount += httpServletRequest.getHeaders(key as String?).toList().size
            }

            return headerCount == count
        }

        private fun headerCountEquals(key: String, count: Int) = httpServletRequest.getHeaders(key).toList().size == count

        fun evaluate(operator: Byte, key: String?, value: String?): Boolean {
            println("Opertaor: $operator")
            if (key != null) {
                if (operator == hasHeader) {
                    println("operator hasHeader")
                    return hasHeader(key)
                }

                if (operator == headerHasValue) {
                    if (value == null) throw ValueCannotBeNullException("Headers.headerHasValue")
                    return headerHasValue(key, value)
                }

                if (operator == matchHeader) {
                    return matchHeader(key.toRegex(RegexOption.IGNORE_CASE))
                }

                if (operator == matchHeaderValue) {
                    if (value == null) throw ValueCannotBeNullException("Headers.matchHeaderValue")
                    return matchHeaderValue(key, value.toRegex())
                }

                if (operator == isHeaderRepeated) {
                    return isHeaderRepeated(key)
                }

                if (operator == headerHasSpecialChar) {
                    return headerHasSpecialChar(key)
                }

                if (operator == totalHeadersCountEquals) {
                    val countConverted = key.toIntOrNull() ?: throw NumberFormatException()
                    return totalHeadersCountEquals(countConverted)
                }

                if (operator == headerCountEquals) {
                    if (value == null) throw ValueCannotBeNullException("Headers.headerCountEquals")
                    val countConverted = value.toIntOrNull() ?: throw NumberFormatException()
                    return headerCountEquals(key, countConverted)
                }

                if (operator == matchAllHeadersValue) {
                    if (value == null) throw ValueCannotBeNullException("Headers.matchAllHeadersValue")
                    return matchAllHeadersValue(key, value.toRegex())
                }

                throw OperatorNotSupportedException(operator, "Header")
            }
            throw KeyCannotBeNullException("Header conditions")
        }
    }
