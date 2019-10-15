package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import helpers.TestSuccessHelpers
import http.Cookies
import http.Http
import http.Parameters
import http.Request
import org.junit.jupiter.api.Test
import result.Action
import kotlin.test.assertEquals

class ServletRequestTests {

    @Test
    fun testRequestAllowCondition(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition, hasSessionCookieCondition, matchValueCondition)

        val getMethodOnlyCondition = Condition(Http.Request, Request.isGetRequest)
        val portCheckCondition = Condition(Http.Request, Request.portEquals, "443")
        val queryStringCheckCondition = Condition(Http.Request, Request.queryStringContains, "username")
        val urlCheckCondition = Condition(Http.Request, Request.urlContains, "Request.kt")

        val conditions = listOf(getMethodOnlyCondition, portCheckCondition, queryStringCheckCondition, urlCheckCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)



        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, mapOf("testRequestAllowCondition" to rule)).affirm())

    }
}