package io.perculator.core.evaluation

import io.perculator.core.model.Condition
import io.perculator.core.request.PerculatorHttpRequest

public fun interface ConditionEvaluator {
    public fun evaluate(condition: Condition, request: PerculatorHttpRequest): Boolean
}
