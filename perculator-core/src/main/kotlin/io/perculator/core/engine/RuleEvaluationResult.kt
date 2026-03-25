package io.perculator.core.engine

import io.perculator.core.model.Action

public data class RuleEvaluationResult(
    val action: Action,
    val ruleName: String? = null,
    val matchedRules: List<String> = emptyList(),
)
