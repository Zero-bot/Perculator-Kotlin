package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import helpers.TestSuccessHelpers
import http.Cookies
import http.Http
import http.Parameters
import org.junit.jupiter.api.Test
import result.Action
import kotlin.test.assertEquals

class ServletCookieTests {

    @Test
    fun testCookieContextAllowCondition(){


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        var httpServletRequest = TestSuccessHelpers().httpServletRequest

       assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, mapOf("testCookieContextAllowCondition" to rule)).affirm())

    }

    @Test
    fun testCookieContextSkipCondition(){


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasRoleParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "role")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.cookieHasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.valueHasOnlyAlphaNumericChar, "SessionId")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        var httpServletRequest = TestSuccessHelpers().httpServletRequest

        assertEquals(Action.Skip, AffirmationBuilderFactory(httpServletRequest, mapOf("testCookieContextSkipCondition" to rule)).affirm())
    }

    @Test
    fun testCookieContextRejectCondition(){


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasCookie, "role")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.cookieHasValue, "role", "user")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.valueHasOnlyAlphaNumericChar, "SessionId")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        val httpServletRequest = TestSuccessHelpers().httpServletRequest

        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, mapOf("testCookieContextRejectCondition" to rule)).affirm())
    }

    @Test
    fun testCookieCountAllowCondition(){


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasCookie, "role")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.countCookies, "2")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.cookieHasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.valueHasOnlyAlphaNumericChar, "SessionId")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        val httpServletRequest = TestSuccessHelpers().httpServletRequest

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, mapOf("testCookieContextRejectCondition" to rule)).affirm())
    }

    @Test
    fun testCookieCountRejectCondition(){
        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasCookie, "role")

        val context = listOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.countCookies, "2")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.cookieHasValue, "role", "user")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.valueHasOnlyAlphaNumericChar, "SessionId")

        val conditions = listOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)
        val httpServletRequest = TestSuccessHelpers().httpServletRequest
        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, mapOf("testCookieCountRejectCondition" to rule)).affirm())
    }

}