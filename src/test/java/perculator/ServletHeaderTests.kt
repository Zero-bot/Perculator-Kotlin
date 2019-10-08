package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import http.Cookies
import http.Headers
import http.Http
import http.Parameters
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import result.Action
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals

class ServletHeaderTests {
    lateinit var httpServletRequest: HttpServletRequest
    private fun <T> List<T>.toEnumeration(): Enumeration<T> {
        return object : Enumeration<T> {
            var count = 0

            override fun hasMoreElements(): Boolean {
                return this.count < size
            }

            override fun nextElement(): T {
                if (this.count < size) {
                    return get(this.count++)
                }
                throw NoSuchElementException("List enumeration asked for more elements than present")
            }
        }
    }
    @Test
    fun testHasHeaderCondition(){
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        val roleCookie = Cookie("role", "admin")
        var cookies = arrayOf<Cookie>(sessionCookie, roleCookie)

        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")

        var headers = mutableMapOf<String, Enumeration<*>>()
        headers["host"] = listOf("www.google.com", "www.google_a.com").toEnumeration()
//        headers["user-agent"] = listOf("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
//        headers["Transfer-Encoding"] = listOf("chunked")
//        headers["Date"] = listOf("Sat, 28 Nov 2009 04:36:25 GMT")
//        headers["Server"] = listOf("LiteSpeed")
//        headers["Cache-Control"] = listOf("max-age=3600, public")

        val hasUserNameParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        val hasPasswordParamCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        val context = arrayOf(hasUserNameParamCondition, hasPasswordParamCondition)

        val hasSessionCookieCondition = Condition(Http.Cookie, Cookies.hasCookie, "SessionId")
        val matchValueCondition = Condition(Http.Cookie, Cookies.matchValue, "SessionId","^[a-z]+$")
        val hasHeaderCondition = Condition(Http.Header, Headers.hasHeader, "host")

        val conditions = arrayOf(hasSessionCookieCondition, matchValueCondition, hasHeaderCondition)

        val rule = Rule(contextConditions = context, conditions = conditions, actionSuccess = Action.Allow, actionFailure = Action.Reject)

        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        Mockito.`when`(httpServletRequest.cookies).thenReturn(cookies)
        Mockito.`when`(httpServletRequest.headerNames).thenReturn(headers.keys as Enumeration<*>)

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, arrayOf(rule)).affirm())

    }
}