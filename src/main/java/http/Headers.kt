package http

import javax.servlet.http.HttpServletRequest

class Headers(val httpServletRequest: HttpServletRequest) {

    companion object{
        const val hasHeader = 0
        const val headerHasValue = 1
        const val matchHeader = 2
        const val matchHeaderValue = 3
        const val isHeaderRepeated = 4
        const val headerHasSpecialChar = 5
        const val totalHeadersCountEquals = 6
        const val headerCountEquals = 7
        const val matchAllHeadersValue = 8
    }

    private fun hasHeader(key: String) = httpServletRequest.headerNames.toList().any { it == key }

    private fun headerHasValue(key: String, value: String) = httpServletRequest.getHeaders(key).asSequence().any { it == value }

    private fun matchHeaderValue(key: String, valueRegex: Regex): Boolean {
        return httpServletRequest.getHeaders(key).asSequence().any { valueRegex.containsMatchIn(it.toString()) }
    }

    private fun matchAllHeadersValue(key: String, valueRegex: Regex): Boolean {
        return httpServletRequest.getHeaders(key).asSequence().all { valueRegex.containsMatchIn(it.toString()) }
    }

    private fun isHeaderRepeated(key: String) = httpServletRequest.getHeaders(key).toList().size > 1

    private fun headerHasSpecialChar(key: String){
        httpServletRequest.getHeaders(key).asSequence().any { "\\W".toRegex().containsMatchIn(it.toString()) }
    }

    private fun totalHeadersCountEquals(count: Int): Boolean {
        var headerCount = 0
        for (key in httpServletRequest.headerNames){
            headerCount += httpServletRequest.getHeaders(key as String?).toList().size
        }

        return headerCount == count
    }

    private fun headerCountEquals(key: String, count: Int) = httpServletRequest.getHeaders(key).toList().size == count
}