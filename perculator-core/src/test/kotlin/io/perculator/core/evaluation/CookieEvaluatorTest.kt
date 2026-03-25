package io.perculator.core.evaluation

import io.mockk.every
import io.mockk.mockk
import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Location
import io.perculator.core.model.Operator.CookieOp
import io.perculator.core.request.PerculatorCookie
import io.perculator.core.request.PerculatorHttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CookieEvaluatorTest {

    private val evaluator = CookieEvaluator()

    private fun mockRequest(cookies: List<PerculatorCookie> = emptyList()): PerculatorHttpRequest {
        val request = mockk<PerculatorHttpRequest>()
        every { request.cookies } returns cookies
        return request
    }

    @Test
    fun `hasCookie returns true when cookie exists`() {
        val request = mockRequest(listOf(PerculatorCookie("session", "abc123")))
        val condition = Condition(Location.Cookie, CookieOp.HasCookie, key = "session")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `hasCookie returns false when missing`() {
        val request = mockRequest(emptyList())
        val condition = Condition(Location.Cookie, CookieOp.HasCookie, key = "session")
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `cookieHasValue checks exact value`() {
        val request = mockRequest(listOf(PerculatorCookie("session", "abc123")))
        val condition = Condition(Location.Cookie, CookieOp.CookieHasValue, key = "session", value = "abc123")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `matchCookie matches regex on names`() {
        val request = mockRequest(listOf(PerculatorCookie("session_id", "abc")))
        val condition = Condition(Location.Cookie, CookieOp.MatchCookie, key = "session.*")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `matchValue checks regex on value`() {
        val request = mockRequest(listOf(PerculatorCookie("session", "abc123")))
        val condition = Condition(Location.Cookie, CookieOp.MatchValue, key = "session", value = "[a-z]+\\d+")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `countCookies checks count`() {
        val cookies = listOf(PerculatorCookie("a", "1"), PerculatorCookie("b", "2"))
        val request = mockRequest(cookies)
        val condition = Condition(Location.Cookie, CookieOp.CountCookies, key = "2")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `valueHasOnlyAlphaNumericChar validates`() {
        val request = mockRequest(listOf(PerculatorCookie("session", "abc123")))
        val condition = Condition(Location.Cookie, CookieOp.ValueHasOnlyAlphaNumericChar, key = "session")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `valueLengthEquals checks length`() {
        val request = mockRequest(listOf(PerculatorCookie("session", "abc")))
        val condition = Condition(Location.Cookie, CookieOp.ValueLengthEquals, key = "session", value = "3")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `missing cookie throws for cookieHasValue`() {
        val request = mockRequest(emptyList())
        val condition = Condition(Location.Cookie, CookieOp.CookieHasValue, key = "missing", value = "val")
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("No such cookie")
    }
}
