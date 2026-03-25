package io.perculator.core.loader

import io.perculator.core.exception.RuleLoadException
import io.perculator.core.model.Action
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class JsonRuleLoaderTest {

    private fun resourcePath(name: String): java.nio.file.Path {
        val url = javaClass.classLoader.getResource("rules/$name")
            ?: throw IllegalStateException("Test resource not found: rules/$name")
        return Paths.get(url.toURI())
    }

    @Test
    fun `loads valid rules file`() {
        val loader = JsonRuleLoader(resourcePath("valid-rules.json"))
        val rules = loader.load()
        assertThat(rules).isNotEmpty()
        assertThat(rules).hasSize(3)
    }

    @Test
    fun `rules are sorted by priority`() {
        val loader = JsonRuleLoader(resourcePath("valid-rules.json"))
        val rules = loader.load()
        val priorities = rules.map { it.priority }
        assertThat(priorities).isSorted()
    }

    @Test
    fun `rule has correct action mapping`() {
        val loader = JsonRuleLoader(resourcePath("valid-rules.json"))
        val rules = loader.load()
        val checkParameter = rules.first { it.name == "checkParameter" }
        assertThat(checkParameter.actionOnMatch).isEqualTo(Action.ALLOW)
        assertThat(checkParameter.actionOnMismatch).isEqualTo(Action.REJECT)
    }

    @Test
    fun `rule has correct conditions`() {
        val loader = JsonRuleLoader(resourcePath("valid-rules.json"))
        val rules = loader.load()
        val checkParameter = rules.first { it.name == "checkParameter" }
        assertThat(checkParameter.conditions).hasSize(2)
    }

    @Test
    fun `rule with context conditions parses correctly`() {
        val loader = JsonRuleLoader(resourcePath("valid-rules.json"))
        val rules = loader.load()
        val contextRule = rules.first { it.name == "contextNotMatch" }
        assertThat(contextRule.contextConditions).hasSize(1)
    }

    @Test
    fun `empty rules file returns empty list`() {
        val loader = JsonRuleLoader(resourcePath("empty-rules.json"))
        val rules = loader.load()
        assertThat(rules).isEmpty()
    }

    @Test
    fun `invalid JSON throws RuleLoadException`() {
        val loader = JsonRuleLoader(resourcePath("invalid-rules.json"))
        assertThatThrownBy { loader.load() }
            .isInstanceOf(RuleLoadException::class.java)
            .hasMessageContaining("Failed to load rules")
    }

    @Test
    fun `missing file throws RuleLoadException`() {
        val loader = JsonRuleLoader(Paths.get("/nonexistent/path.json"))
        assertThatThrownBy { loader.load() }
            .isInstanceOf(RuleLoadException::class.java)
            .hasMessageContaining("Rules file not found")
    }
}
