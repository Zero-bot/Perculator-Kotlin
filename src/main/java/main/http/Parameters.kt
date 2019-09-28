package main.http

import main.exception.NoSuchParameterException
import javax.servlet.http.HttpServletRequest

class Parameters(val httpServletRequest: HttpServletRequest) {

    companion object{
        public final val hasKey:Byte = 1
        public final val hasValue:Byte = 2
        public final val match:Byte = 3
    }

    private val parameters: Map<String, String>
        get() = this.httpServletRequest.parameterMap as Map<String, String>

    public fun hasKey(key: String) = parameters.containsKey(key)

    public fun hasValue(key: String, value: String) = parameters[key] == value

    public fun matchValue(key: Regex, value: Regex): Boolean {
        parameters.filter { it.key.matches(key) }.forEach{
            println(Pair(it.key, it.value))
            if(!it.value.matches(value))
                return false
        }
        return true
    }

    public fun match(key: String, regex: Regex):Boolean {
        val result = parameters[key]
        if(result!=null){
            return result.matches(regex)
        }
        return false
    }

    public fun notMatch(key: String, regex: Regex) = match(key, regex).not()

    public fun count(count: Int) = parameters.size == count

    public fun matchKey(regex: Regex) = parameters.filter { it.key.matches(regex) }.isNotEmpty()

}