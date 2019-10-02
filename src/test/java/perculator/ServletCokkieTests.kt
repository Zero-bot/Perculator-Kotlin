package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import http.Cookies
import http.Http
import http.Parameters
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import result.Action
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServletCookieTests {
    lateinit var httpServletRequest: HttpServletRequest

    @Test
    fun testCookieContextAllowCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        sessionCookie.secure = true
        sessionCookie.path = "/"
        sessionCookie.maxAge = 100
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasKey, "password")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasKey, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)

       assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())

    }

    @Test
    fun testCookieContextSkipCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        sessionCookie.secure = true
        sessionCookie.path = "/"
        sessionCookie.maxAge = 100
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        val hasRoleParamCondition = Condition(Http.Parameter, Parameters.hasKey, "role")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasKey, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.hasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.hasOnlyAlphaNumericChar, "SessionId")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)

        assertEquals(Action.Skip, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())
    }

    @Test
    fun testCookieContextRejectCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz!")
        sessionCookie.secure = true
        sessionCookie.path = "/"
        sessionCookie.maxAge = 100
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasKey, "role")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasKey, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.hasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.hasOnlyAlphaNumericChar, "SessionId")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)

        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())
    }

    @Test
    fun testCookieCountAllowCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        sessionCookie.secure = true
        sessionCookie.path = "/"
        sessionCookie.maxAge = 100
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasKey, "role")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.count, "2")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.hasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.hasOnlyAlphaNumericChar, "SessionId")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())
    }

    @Test
    fun testCookieCountRejectCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        sessionCookie.secure = true
        sessionCookie.path = "/"
        sessionCookie.maxAge = 100
        val roleCookie = Cookie("role", "admin")
        val optionSCookie = Cookie("options", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie, optionSCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        val hasRoleParamCondition = Condition(Http.Cookie, Cookies.hasKey, "role")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition, hasRoleParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.count, "2")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasValueCondition = Condition(Http.Cookie, Cookies.hasValue, "role", "admin")
        val sessionHasOnlyAlphanumericCondition = Condition(Http.Cookie, Cookies.hasOnlyAlphaNumericChar, "SessionId")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition, hasValueCondition, sessionHasOnlyAlphanumericCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)

        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())
    }

}