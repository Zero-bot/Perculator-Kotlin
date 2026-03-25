package io.perculator.core

import io.perculator.core.engine.RuleEngine
import io.perculator.core.engine.RuleEvaluationResult
import io.perculator.core.loader.FileWatchingRuleLoader
import io.perculator.core.loader.JsonRuleLoader
import io.perculator.core.loader.RuleLoader
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache

public class Perculator private constructor(
    private val ruleLoader: RuleLoader,
    private val engine: RuleEngine,
) : AutoCloseable {

    public fun evaluate(request: PerculatorHttpRequest): RuleEvaluationResult {
        val rules = ruleLoader.load()
        return engine.evaluate(request, rules)
    }

    override fun close() {
        if (ruleLoader is AutoCloseable) {
            ruleLoader.close()
        }
    }

    public companion object {
        public fun create(config: PerculatorConfig): Perculator {
            val regexCache = RegexCache()
            val engine = RuleEngine(RuleEngine.defaultEvaluators(regexCache))
            val jsonLoader = JsonRuleLoader(config.rulesPath)
            val loader: RuleLoader = if (config.watchRules) {
                FileWatchingRuleLoader(jsonLoader, config.rulesPath)
            } else {
                jsonLoader
            }
            return Perculator(loader, engine)
        }

        public fun create(ruleLoader: RuleLoader): Perculator {
            val regexCache = RegexCache()
            val engine = RuleEngine(RuleEngine.defaultEvaluators(regexCache))
            return Perculator(ruleLoader, engine)
        }

        public fun create(ruleLoader: RuleLoader, engine: RuleEngine): Perculator {
            return Perculator(ruleLoader, engine)
        }
    }
}
