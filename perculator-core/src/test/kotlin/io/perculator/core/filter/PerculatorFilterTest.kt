package io.perculator.core.filter

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.perculator.core.Perculator
import io.perculator.core.engine.RuleEvaluationResult
import io.perculator.core.model.Action
import io.perculator.core.request.PerculatorHttpRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class PerculatorFilterTest {

    private fun mockServletRequest(
        method: String = "GET",
        uri: String = "/test",
    ): HttpServletRequest {
        val request = mockk<HttpServletRequest>(relaxed = true)
        every { request.method } returns method
        every { request.requestURI } returns uri
        every { request.inputStream } returns createServletInputStream(ByteArray(0))
        every { request.characterEncoding } returns "UTF-8"
        return request
    }

    private fun createServletInputStream(data: ByteArray): ServletInputStream {
        val bais = ByteArrayInputStream(data)
        return object : ServletInputStream() {
            override fun read(): Int = bais.read()
            override fun isFinished(): Boolean = bais.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: jakarta.servlet.ReadListener?) {}
        }
    }

    @Test
    fun `REJECT action returns 403`() {
        val perculator = mockk<Perculator>()
        every { perculator.evaluate(any<PerculatorHttpRequest>()) } returns
            RuleEvaluationResult(Action.REJECT, "test-rule")

        val filter = PerculatorFilter(perculator)
        val request = mockServletRequest()
        val response = mockk<HttpServletResponse>(relaxed = true)
        val chain = mockk<FilterChain>(relaxed = true)

        filter.doFilter(request, response, chain)

        verify { response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden") }
        verify(exactly = 0) { chain.doFilter(any(), any()) }
    }

    @Test
    fun `ALLOW action passes through filter chain`() {
        val perculator = mockk<Perculator>()
        every { perculator.evaluate(any<PerculatorHttpRequest>()) } returns
            RuleEvaluationResult(Action.ALLOW, "test-rule")

        val filter = PerculatorFilter(perculator)
        val request = mockServletRequest()
        val response = mockk<HttpServletResponse>(relaxed = true)
        val chain = mockk<FilterChain>(relaxed = true)

        filter.doFilter(request, response, chain)

        verify { chain.doFilter(any(), any()) }
        verify(exactly = 0) { response.sendError(any()) }
        verify(exactly = 0) { response.sendError(any(), any()) }
    }

    @Test
    fun `LOG action passes through filter chain`() {
        val perculator = mockk<Perculator>()
        every { perculator.evaluate(any<PerculatorHttpRequest>()) } returns
            RuleEvaluationResult(Action.LOG, "test-rule")

        val filter = PerculatorFilter(perculator)
        val request = mockServletRequest()
        val response = mockk<HttpServletResponse>(relaxed = true)
        val chain = mockk<FilterChain>(relaxed = true)

        filter.doFilter(request, response, chain)

        verify { chain.doFilter(any(), any()) }
    }
}
