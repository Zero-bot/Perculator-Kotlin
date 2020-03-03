package perculator

import affirm.AffirmationBuilderFactory
import affirm.Perculator
import affirm.rule.Condition
import affirm.rule.Rule
import helpers.TestJsonHelpers
import helpers.TestSuccessHelpers
import http.Headers
import http.Http
import http.Parameters
import kotlinx.serialization.ImplicitReflectionSerializer
import org.junit.jupiter.api.Test
import result.Action
import kotlin.test.assertEquals

class ServletJsonRequestBodyTests {
    @ImplicitReflectionSerializer
    @Test
    fun testJsonRuleCheckHasParameter(){
        val httpServletRequest = TestJsonHelpers().httpServletRequest
        val hasUserName = Condition(Http.Header, Headers.hasHeader, "content-type")
        val rule = Rule(
            contextConditions = null,
            conditions = listOf(hasUserName),
            actionFailure = Action.Reject,
            actionSuccess = Action.Allow
        )
        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, mapOf("hasUserName" to rule)).affirm())
    }
}