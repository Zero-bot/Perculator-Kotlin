package io.perculator.core.loader

import io.perculator.core.model.Rule

public interface RuleLoader {
    public fun load(): List<Rule>
}
