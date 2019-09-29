package main.affirm.rule

import main.http.Http
import main.http.Parameters
import javax.servlet.http.HttpServletRequest

class Condition {
    var location: Byte = -1
        get() = this.location
    var operator: Byte = -1
        get() = this.operator
    var key: String
        get() = this.key
    var value: String?
        get() = this.value

    constructor(location: Byte, operator: Byte, key: String, value: String){
        this.location = location
        this.operator = operator
        this.key = key
        this.value = value
    }

    constructor(location: Byte, operator: Byte, key: String){
        this.location = location
        this.operator = operator
        this.key = key
        this.value = null
    }

    fun evaluate(httpServletRequest: HttpServletRequest){

    }

    private fun evaluateParameterCondition(parameters: Parameters){
        if (this.operator == Parameters.hasKey){

        }
    }
}