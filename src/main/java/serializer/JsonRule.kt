package serializer

import affirm.rule.Condition
import affirm.rule.Rule
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import result.Action
import java.io.File

@Serializable
data class JsonRule(val context: List<JsonCondition>? = null, val conditions: List<JsonCondition>, val action: JsonAction){
    fun toRule(): Rule {
        var ruleContext = mutableListOf<Condition>()
        var ruleCondition = mutableListOf<Condition>()
        if(context!=null){
            for(jsonContext in context) ruleContext.add(jsonContext.generateConditionFromJsonObject())
        }
        for(condition in conditions) ruleCondition.add(condition.generateConditionFromJsonObject())
        return Rule(ruleContext , ruleCondition, action.success, action.failure)
    }
}

fun main(){
    var json  = Json(JsonConfiguration.Stable)
    var jsonString = json.parseJson(File("/Users/kalki/IdeaProjects/Perculator/src/main/resources/Rules.json").readText())

    var hasUserNameParamConditionJson = """{"location":"Http.Cookie","operator":"Cookie.match","key":"sessionId","value":"[\\d++]"}"""
    var hasPasswordParamConditionJson = """{"location":"Http.Parameter","operator":Parameter.hasParameter,"key":"password","value":null}"""
    var actionJson = """{"success":"Allow","failure":"Reject"}"""

    var hasPasswordParamConditionJsonObj = json.parse(JsonCondition.serializer(), hasPasswordParamConditionJson)
    var hasUserNameParamConditionJsonObj = json.parse(JsonCondition.serializer(), hasUserNameParamConditionJson)

    var context = listOf<JsonCondition>(hasPasswordParamConditionJsonObj, hasUserNameParamConditionJsonObj)
    var conditions = listOf<JsonCondition>(hasUserNameParamConditionJsonObj, hasPasswordParamConditionJsonObj)
    var action = JsonAction(Action.Allow, Action.Reject)

    println(json.stringify(JsonAction.serializer(), action))
    println(hasUserNameParamConditionJsonObj.value)
    val rule = JsonRule( conditions =  conditions, action = action)

    val rules = JsonRuleSet(mapOf("checkCookie" to rule, "another" to rule))

    println(json.stringify(JsonRuleSet.serializer(), rules))

//    print(jsonContext.generateConditionFromJsonObject().operator)

}