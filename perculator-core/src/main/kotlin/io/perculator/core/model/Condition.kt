package io.perculator.core.model

public data class Condition(
    val location: Location,
    val operator: Operator,
    val key: String? = null,
    val value: String? = null,
)
