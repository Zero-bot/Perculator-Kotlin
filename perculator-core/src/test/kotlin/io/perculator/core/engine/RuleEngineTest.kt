package io.perculator.core.engine

import io.mockk.every
import io.mockk.mockk
import io.perculator.core.model.*
import io.perculator.core.request.PerculatorHttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RuleEngineTest {

    private val engine = RuleEngine()

    private fun mockRequest(params: Map<String, Array<String>> = emptyMap(), method: String = "GET"): PerculatorHttpRequest {
        val request = mockk<PerculatorHttpRequest>()
        every { request.parameterMap } returns params
        every { request.method } returns method
        every { request.requestUri } returns "/test"
        every { request.requestUrl } returns "http://localhost/test"
        every { request.queryString } returns null
        every { request.scheme } returns "http"
        every { request.protocol } returns "HTTP/1.1"
        every { request.serverName } returns "localhost"
        every { request.serverPort } returns 8080
        every { request.headerNames } returns emptyList()
        every { request.cookies } returns emptyList()
        return request
    }

    @Test
    fun `empty rules returns ALLOW`() {
        val result = engine.evaluate(mockRequest(), emptyList())
        assertThat(result.action).isEqualTo(Action.ALLOW)
    }

    @Test
    fun `single matching rule returns actionOnMatch`() {
        val rule = Rule(
            name = "test",
            conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
            actionOnMatch = Action.ALLOW,
            actionOnMismatch = Action.REJECT,
        )
        val result = engine.evaluate(mockRequest(), listOf(rule))
        assertThat(result.action).isEqualTo(Action.ALLOW)
        assertThat(result.ruleName).isEqualTo("test")
    }

    @Test
    fun `non-matching rule returns actionOnMismatch`() {
        val rule = Rule(
            name = "test",
            conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsPostRequest)),
            actionOnMatch = Action.ALLOW,
            actionOnMismatch = Action.REJECT,
        )
        val result = engine.evaluate(mockRequest(method = "GET"), listOf(rule))
        assertThat(result.action).isEqualTo(Action.REJECT)
    }

    @Test
    fun `short-circuits on REJECT`() {
        val rules = listOf(
            Rule(
                name = "rejector",
                priority = 1,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.REJECT,
                actionOnMismatch = Action.ALLOW,
            ),
            Rule(
                name = "allower",
                priority = 2,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.ALLOW,
                actionOnMismatch = Action.REJECT,
            ),
        )
        val result = engine.evaluate(mockRequest(), rules)
        assertThat(result.action).isEqualTo(Action.REJECT)
        assertThat(result.ruleName).isEqualTo("rejector")
        assertThat(result.matchedRules).hasSize(1)
    }

    @Test
    fun `rules sorted by priority`() {
        val rules = listOf(
            Rule(
                name = "low-priority",
                priority = 10,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.REJECT,
                actionOnMismatch = Action.ALLOW,
            ),
            Rule(
                name = "high-priority",
                priority = 1,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.ALLOW,
                actionOnMismatch = Action.REJECT,
            ),
        )
        val result = engine.evaluate(mockRequest(), rules)
        // High priority runs first (ALLOW), then low priority (REJECT) short-circuits
        assertThat(result.action).isEqualTo(Action.REJECT)
    }

    @Test
    fun `context conditions skip rule when not met`() {
        val request = mockRequest(params = mapOf("name" to arrayOf("john")))
        val rule = Rule(
            name = "contextual",
            contextConditions = listOf(
                Condition(Location.Parameter, Operator.ParameterOp.HasParameter, key = "noSuchParam")
            ),
            conditions = listOf(
                Condition(Location.Parameter, Operator.ParameterOp.HasParameter, key = "name")
            ),
            actionOnMatch = Action.REJECT,
            actionOnMismatch = Action.ALLOW,
        )
        val result = engine.evaluate(request, listOf(rule))
        // Context not met -> SKIP, which is higher priority than default ALLOW... but SKIP has lower priority
        assertThat(result.action).isEqualTo(Action.ALLOW)
    }

    @Test
    fun `multiple rules all evaluated when no REJECT`() {
        val rules = listOf(
            Rule(
                name = "rule1",
                priority = 1,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.LOG,
                actionOnMismatch = Action.ALLOW,
            ),
            Rule(
                name = "rule2",
                priority = 2,
                conditions = listOf(Condition(Location.Request, Operator.RequestOp.IsGetRequest)),
                actionOnMatch = Action.ALLOW,
                actionOnMismatch = Action.REJECT,
            ),
        )
        val result = engine.evaluate(mockRequest(), rules)
        assertThat(result.action).isEqualTo(Action.LOG)
        assertThat(result.matchedRules).hasSize(2)
    }
}
