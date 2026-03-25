package io.perculator.core.evaluation

import io.mockk.every
import io.mockk.mockk
import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Location
import io.perculator.core.model.Operator.RequestOp
import io.perculator.core.request.PerculatorHttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RequestEvaluatorTest {

    private val evaluator = RequestEvaluator()

    private fun mockRequest(
        method: String = "GET",
        uri: String = "/test",
        url: String = "http://localhost/test",
        queryString: String? = null,
        scheme: String = "http",
        protocol: String = "HTTP/1.1",
        serverName: String = "localhost",
        serverPort: Int = 8080,
    ): PerculatorHttpRequest {
        val request = mockk<PerculatorHttpRequest>()
        every { request.method } returns method
        every { request.requestUri } returns uri
        every { request.requestUrl } returns url
        every { request.queryString } returns queryString
        every { request.scheme } returns scheme
        every { request.protocol } returns protocol
        every { request.serverName } returns serverName
        every { request.serverPort } returns serverPort
        return request
    }

    @Test
    fun `isGetRequest returns true for GET`() {
        val request = mockRequest(method = "GET")
        val condition = Condition(Location.Request, RequestOp.IsGetRequest)
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `isGetRequest returns false for POST`() {
        val request = mockRequest(method = "POST")
        val condition = Condition(Location.Request, RequestOp.IsGetRequest)
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `isPostRequest returns true for POST`() {
        val request = mockRequest(method = "POST")
        val condition = Condition(Location.Request, RequestOp.IsPostRequest)
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `methodEquals matches case-insensitively`() {
        val request = mockRequest(method = "GET")
        val condition = Condition(Location.Request, RequestOp.MethodEquals, key = "get")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `methodNotEquals returns true for different method`() {
        val request = mockRequest(method = "GET")
        val condition = Condition(Location.Request, RequestOp.MethodNotEquals, key = "POST")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `uriEquals matches exact URI`() {
        val request = mockRequest(uri = "/api/v1/users")
        val condition = Condition(Location.Request, RequestOp.UriEquals, key = "/api/v1/users")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `uriContains checks substring`() {
        val request = mockRequest(uri = "/api/v1/users")
        val condition = Condition(Location.Request, RequestOp.UriContains, key = "v1")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `uriMatch uses regex`() {
        val request = mockRequest(uri = "/api/v1/users")
        val condition = Condition(Location.Request, RequestOp.UriMatch, key = "/api/v\\d+/.*")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `queryStringContains throws when query string null`() {
        val request = mockRequest(queryString = null)
        val condition = Condition(Location.Request, RequestOp.QueryStringContains, key = "test")
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("Query string is null")
    }

    @Test
    fun `queryStringEquals matches`() {
        val request = mockRequest(queryString = "name=john")
        val condition = Condition(Location.Request, RequestOp.QueryStringEquals, key = "name=john")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `schemeEquals matches`() {
        val request = mockRequest(scheme = "https")
        val condition = Condition(Location.Request, RequestOp.SchemeEquals, key = "https")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `portEquals matches`() {
        val request = mockRequest(serverPort = 443)
        val condition = Condition(Location.Request, RequestOp.PortEquals, key = "443")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `portNotEquals returns true for different port`() {
        val request = mockRequest(serverPort = 8080)
        val condition = Condition(Location.Request, RequestOp.PortNotEquals, key = "443")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `serverEquals matches`() {
        val request = mockRequest(serverName = "example.com")
        val condition = Condition(Location.Request, RequestOp.ServerEquals, key = "example.com")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `serverMatch uses regex`() {
        val request = mockRequest(serverName = "api.example.com")
        val condition = Condition(Location.Request, RequestOp.ServerMatch, key = ".*\\.example\\.com")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `null key throws for operators requiring key`() {
        val request = mockRequest()
        val condition = Condition(Location.Request, RequestOp.MethodEquals, key = null)
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("Key cannot be null")
    }
}
