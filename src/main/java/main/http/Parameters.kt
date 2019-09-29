package main.http

import main.exception.OperatorNotSupportedException
import main.exception.ValueCannotBeNullException
import java.lang.NumberFormatException
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

    private val parameters: Map<*, *>
        get() = this.httpServletRequest.parameterMap as Map<*, *>

    public fun hasKey(key: String) = parameters.containsKey(key)

    public fun matchKey(keyRegex: Regex) = parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.isNotEmpty()

    public fun hasValue(key: String, value: String):Boolean{
        if(hasKey(key))
            return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, value) }
        return false
    }

    public fun matchValue(keyRegex: Regex, valueRegex: Regex): Boolean {
        if (matchKey(keyRegex))
            return parameters.filterKeys { keyRegex.containsMatchIn(it as String) }.all { allMatch(it.value as Array<Any>, valueRegex) }
        return false
    }

    public fun match(key: String, regex: Regex): Boolean {
        if(hasKey(key))
            return parameters.filterKeys { it == key }.all { allMatch(it.value as Array<Any>, regex) }
        return false
    }

    public fun count(count: Int) = parameters.size == count

    private fun allMatch(value: Array<Any>, regex: Regex): Boolean {
        return value.all { regex.containsMatchIn(it as CharSequence) }
    }

    private fun allMatch(value: Array<Any>, match: String): Boolean {
        return value.all { match == (it as CharSequence) }
    }

    fun evaluate(operator: Byte, key: String, value: String?): Boolean {
        if(operator == hasKey) return hasKey(key)

        if(operator == matchKey) return matchKey(key.toRegex())

        if(operator == hasValue){
            if (value == null) throw ValueCannotBeNullException("Parameter.hasValue")
            return hasValue(key, value)
        }

        if(operator == matchValue){
            if (value == null) throw ValueCannotBeNullException("Parameter.matchValue")
            return matchValue(key.toRegex(), value.toRegex())
        }

        if(operator == match ){
            if (value == null) throw ValueCannotBeNullException("Parameter.Match")
            return  match(key, value.toRegex())
        }

        if(operator == count){
            val countConverted = key.toIntOrNull() ?: throw NumberFormatException()
            return count(countConverted)
        }

        throw OperatorNotSupportedException(operator, "Parameter")
    }
}