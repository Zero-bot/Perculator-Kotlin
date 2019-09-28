package main.http

import main.exception.NoSuchParameterException
import javax.servlet.http.HttpServletRequest

class Parameters(val httpServletRequest: HttpServletRequest) {

    companion object {
        public final val hasKey: Byte = 1
        public final val matchKey: Byte = 2
        public final val hasValue: Byte = 3
        public final val matchValue: Byte = 4
        public final val match: Byte = 5
        public final val count: Byte = 6
    }

    private val parameters: Map<String, String>
        get() = this.httpServletRequest.parameterMap as Map<String, String>

    public fun hasKey(key: String) = parameters.containsKey(key)

    public fun matchKey(keyRegex: Regex) = parameters.filterKeys { keyRegex.containsMatchIn(it) }.isNotEmpty()

    public fun hasValue(key: String, value: String):Boolean{
        if(hasKey(key))
            return parameters.filterKeys { it == key }.all { it.value == value }
        return false
    }

    public fun matchValue(keyRegex: Regex, valueRegex: Regex): Boolean {
        if (matchKey(keyRegex))
            return parameters.filterKeys { keyRegex.containsMatchIn(it) }.all { valueRegex.containsMatchIn(it.value) }
        return false
    }

    public fun match(key: String, regex: Regex): Boolean {
        if(hasKey(key))
            return parameters.filterKeys { it == key }.all { regex.containsMatchIn(it.value) }
        return false
    }

    public fun count(count: Int) = parameters.size == count

}