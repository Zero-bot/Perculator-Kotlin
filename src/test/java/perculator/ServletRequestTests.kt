package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import http.Cookies
import http.Http
import http.Parameters
import http.Request
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import result.Action
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals

class ServletRequestTests {
    lateinit var httpServletRequest: HttpServletRequest

    @Test
    fun testRequestAllowCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        var method = "GET"
        var port = 443
        var queryString = "username=c+d&role=e+f"
        var url = StringBuffer("https://github.com/Zero-bot/Perculator-Kotlin/blob/master/src/main/java/http/Request.kt")
        var uri = "Zero-bot/Perculator-Kotlin/blob/master/src/main/java/http/Request.kt"
        var scheme = "https"
        var protocol = "HTTP 1.1"
        var server = "github.com"


        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition, hasSessionCookieCondition, matchValueCondition)

        val getMethodOnlyCondition = Condition(Http.Request, Request.isGetRequest)
        val portCheckCondition = Condition(Http.Request, Request.portEquals, "443")
        val queryStringCheckCondition = Condition(Http.Request, Request.queryStringContains, "username")
        val urlCheckCondition = Condition(Http.Request, Request.urlContains, "Request.kt")

        val conditions = arrayOf(getMethodOnlyCondition, portCheckCondition, queryStringCheckCondition, urlCheckCondition)
        
        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)
        Mockito.`when`(httpServletRequest.method).thenReturn(method)
        Mockito.`when`(httpServletRequest.serverPort).thenReturn(port)
        Mockito.`when`(httpServletRequest.queryString).thenReturn(queryString)
        Mockito.`when`(httpServletRequest.requestURL).thenReturn(url)
        Mockito.`when`(httpServletRequest.requestURI).thenReturn(uri)
        Mockito.`when`(httpServletRequest.scheme).thenReturn(scheme)
        Mockito.`when`(httpServletRequest.serverName).thenReturn(server)
        Mockito.`when`(httpServletRequest.protocol).thenReturn(protocol)

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())

    }
}