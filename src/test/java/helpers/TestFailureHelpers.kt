package helpers

import org.mockito.Mockito
import wrappers.MutableHttpServletRequest
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

class TestFailureHelpers {
    var httpServletRequest: MutableHttpServletRequest

    init {
        val sessionCookie = Cookie("SessionId", "abcdefghijklmnopqrstuvwxyz")
        val roleCookie = Cookie("role", "admin")
        val directoryCookie = Cookie("directory", "\\etc")
        val cookies = arrayOf<Cookie>(sessionCookie, roleCookie, directoryCookie)

        val parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")
        parameters["url"] = arrayOf("second", "value")
        parameters["filter"] = arrayOf("' or 1=1--")

        var method = "GET"
        var port = 443
        var queryString = "username=c+d&role=e+f"
        var url = StringBuffer("https://github.com/Zero-bot/Perculator-Kotlin/blob/master/src/main/java/http/Request.kt")
        var uri = "Zero-bot/Perculator-Kotlin/blob/master/src/main/java/http/Request.kt"
        var scheme = "https"
        var protocol = "HTTP 1.1"
        var server = "github.com"

        var headers = mutableMapOf<String, Enumeration<*>>()
        headers["host"] = listOf("www.google.com", "www.google_a.com").toEnumeration()
        headers["user-agent"] = listOf("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36").toEnumeration()
        headers["Transfer-Encoding"] = listOf("chunked").toEnumeration()
        headers["Date"] = listOf("Sat, 28 Nov 2009 04:36:25 GMT").toEnumeration()
        headers["Server"] = listOf("LiteSpeed").toEnumeration()
        headers["Cache-Control"] = listOf("max-age=3600, public").toEnumeration()


        this.httpServletRequest = Mockito.mock(MutableHttpServletRequest::class.java)
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
        Mockito.`when`(httpServletRequest.headerNames).thenReturn(headers.keys.toList().toEnumeration())
    }

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
}