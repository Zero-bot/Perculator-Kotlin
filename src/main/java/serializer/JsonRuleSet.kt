package serializer

import affirm.rule.Condition
import affirm.rule.Rule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonRuleSet(@SerialName("Rules") val rules: Map<String, JsonRule> ){
    fun toRulesMap(): Map<String, Rule> = rules.keys.associateWith { (rules[it] ?: error("JsonRules map returned null for key $it")).toRule() }
}