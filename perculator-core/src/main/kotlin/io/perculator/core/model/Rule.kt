package io.perculator.core.model

public data class Rule(
    val name: String,
    val contextConditions: List<Condition> = emptyList(),
    val conditions: List<Condition>,
    val actionOnMatch: Action,
    val actionOnMismatch: Action,
    val priority: Int = Int.MAX_VALUE,
)
