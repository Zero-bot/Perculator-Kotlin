package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import helpers.TestSuccessHelpers
import http.Cookies
import http.Headers
import http.Http
import http.Parameters
import org.junit.jupiter.api.Test
import result.Action
import kotlin.test.assertEquals

class ServletHeaderTests {

    @Test
    fun testHasHeaderCondition(){

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasHeaderCondition = Condition(Http.Header, Headers.hasHeader, "host")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition, hasHeaderCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        val httpServletRequest = TestSuccessHelpers().httpServletRequest
        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, mapOf("testHasHeaderCondition" to rule)).affirm())

    }
}