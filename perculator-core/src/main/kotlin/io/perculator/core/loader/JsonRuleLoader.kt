package io.perculator.core.loader

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.perculator.core.exception.RuleLoadException
import io.perculator.core.model.*
import java.nio.file.Path

public class JsonRuleLoader(
    private val source: Path,
) : RuleLoader {

    private val objectMapper = jacksonObjectMapper()

    override fun load(): List<Rule> {
        try {
            val file = source.toFile()
            if (!file.exists()) throw RuleLoadException("Rules file not found: $source")
            val jsonRuleSet: JsonRuleSetDto = objectMapper.readValue(file)
            return jsonRuleSet.toRules()
        } catch (e: RuleLoadException) {
            throw e
        } catch (e: Exception) {
            throw RuleLoadException("Failed to load rules from $source", e)
        }
    }
}

internal data class JsonRuleSetDto(
    @JsonProperty("Rules") val rules: Map<String, JsonRuleDto>,
) {
    fun toRules(): List<Rule> =
        rules.entries
            .map { (name, dto) -> dto.toRule(name) }
            .sortedBy { it.priority }
}

internal data class JsonRuleDto(
    val context: List<JsonConditionDto>? = null,
    val conditions: List<JsonConditionDto>,
    val action: JsonActionDto,
    val priority: Int = Int.MAX_VALUE,
) {
    fun toRule(name: String): Rule = Rule(
        name = name,
        contextConditions = context?.map { it.toCondition() } ?: emptyList(),
        conditions = conditions.map { it.toCondition() },
        actionOnMatch = action.success,
        actionOnMismatch = action.failure,
        priority = priority,
    )
}

internal data class JsonConditionDto(
    val location: String,
    val operator: String,
    val key: String? = null,
    val value: String? = null,
) {
    fun toCondition(): Condition {
        val loc = Location.fromString(location)
        val op = Operator.fromString(loc, operator)
        return Condition(location = loc, operator = op, key = key, value = value)
    }
}

internal data class JsonActionDto(
    val success: Action,
    val failure: Action,
)
