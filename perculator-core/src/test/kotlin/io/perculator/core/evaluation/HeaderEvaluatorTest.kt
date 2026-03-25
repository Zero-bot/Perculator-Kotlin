package io.perculator.core.evaluation

import io.mockk.every
import io.mockk.mockk
import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Location
import io.perculator.core.model.Operator.HeaderOp
import io.perculator.core.request.PerculatorHttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class HeaderEvaluatorTest {

    private val evaluator = HeaderEvaluator()

    private fun mockRequest(headers: Map<String, List<String>> = emptyMap()): PerculatorHttpRequest {
        val request = mockk<PerculatorHttpRequest>()
        every { request.headerNames } returns headers.keys.toList()
        headers.forEach { (name, values) ->
            every { request.getHeaders(name) } returns values
        }
        return request
    }

    @Test
    fun `hasHeader returns true when header exists`() {
        val request = mockRequest(mapOf("Content-Type" to listOf("application/json")))
        val condition = Condition(Location.Header, HeaderOp.HasHeader, key = "Content-Type")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `hasHeader is case-insensitive`() {
        val request = mockRequest(mapOf("content-type" to listOf("application/json")))
        val condition = Condition(Location.Header, HeaderOp.HasHeader, key = "Content-Type")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `headerHasValue checks exact value`() {
        val request = mockRequest(mapOf("Accept" to listOf("text/html", "application/json")))
        val condition = Condition(Location.Header, HeaderOp.HeaderHasValue, key = "Accept", value = "text/html")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `matchHeader uses regex with case insensitive`() {
        val request = mockRequest(mapOf("X-Custom-Header" to listOf("value")))
        val condition = Condition(Location.Header, HeaderOp.MatchHeader, key = "x-custom.*")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `isHeaderRepeated detects multiple values`() {
        val request = mockRequest(mapOf("Accept" to listOf("text/html", "application/json")))
        val condition = Condition(Location.Header, HeaderOp.IsHeaderRepeated, key = "Accept")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `isHeaderRepeated returns false for single value`() {
        val request = mockRequest(mapOf("Accept" to listOf("text/html")))
        val condition = Condition(Location.Header, HeaderOp.IsHeaderRepeated, key = "Accept")
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `totalHeadersCountEquals counts all header values`() {
        val request = mockRequest(mapOf(
            "Accept" to listOf("text/html"),
            "Host" to listOf("localhost"),
        ))
        val condition = Condition(Location.Header, HeaderOp.TotalHeadersCountEquals, key = "2")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `headerCountEquals checks specific header count`() {
        val request = mockRequest(mapOf("Accept" to listOf("text/html", "application/json")))
        val condition = Condition(Location.Header, HeaderOp.HeaderCountEquals, key = "Accept", value = "2")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `matchAllHeadersValue validates all values match regex`() {
        val request = mockRequest(mapOf("Accept" to listOf("text/html", "text/plain")))
        val condition = Condition(Location.Header, HeaderOp.MatchAllHeadersValue, key = "Accept", value = "text/.*")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `missing header throws for headerHasValue`() {
        val request = mockRequest(emptyMap())
        val condition = Condition(Location.Header, HeaderOp.HeaderHasValue, key = "Missing", value = "val")
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("No such header")
    }
}
