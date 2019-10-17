package serializer

import affirm.rule.Condition
import affirm.rule.Rule
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import result.Action
import java.io.File

@Serializable
data class JsonRule(val context: List<JsonCondition>? = null, val conditions: List<JsonCondition>, val action: JsonAction, val priority: Int = Int.MAX_VALUE){
    fun toRule(): Rule {
        var ruleContext = mutableListOf<Condition>()
        var ruleCondition = mutableListOf<Condition>()
        if(context!=null){
            for(jsonContext in context) ruleContext.add(jsonContext.generateConditionFromJsonObject())
        }
        for(condition in conditions) ruleCondition.add(condition.generateConditionFromJsonObject())
        return Rule(contextConditions = ruleContext , conditions = ruleCondition, actionSuccess = action.success, actionFailure = action.failure, priority = priority)
    }
}

