package http

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.InvalidJsonException
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import wrappers.MutableHttpServletRequest

class JsonParameters(val httpServletRequest: MutableHttpServletRequest) {
    private val json: Any

    companion object {
        const val hasParameter: Byte = 1
        const val matchParameter: Byte = 2
        const val hasParameterWithValue: Byte = 3
        const val matchParameterWithValue: Byte = 4
        const val match: Byte = 5
        const val countParameters: Byte = 6
        const val valueHasOnlyAlphaNumericChar: Byte = 7
    }

    init {
        json = parseJsonBody()
    }

    private fun parseJsonBody(): Any {
        return try {
            val configuration = Configuration.defaultConfiguration()
            configuration.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
            JsonPath.using(configuration).parse(httpServletRequest.getBody())
        } catch (e: InvalidJsonException){
            throw e
        }
    }

    private fun hasParameter(key: String): Boolean {
        return JsonPath.read<Any>(json, key) != null
    }
}