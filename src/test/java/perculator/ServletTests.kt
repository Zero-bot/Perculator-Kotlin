package perculator

import main.http.Http
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ServletTests {
    lateinit var httpServletRequest: HttpServletRequest

    @Test
    fun testServlet(){
        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        var parameters = mutableMapOf("name" to "Marimuthu Mahalingam", "password" to "password")
        `when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        var http = Http(this.httpServletRequest)
        assertTrue { http.parameters.hasKey("name") }
        assertEquals(true, http.parameters.match("password", Regex("\\w")))
        assertEquals(false, http.parameters.hasKey("random"))
        assertEquals(true, http.parameters.hasValue("name", "Marimuthu Mahalingam"))
        assertEquals(true, http.parameters.count(2))
        assertEquals(true, http.parameters.matchValue(Regex("\\w"), Regex("\\w")))
        assertFalse { http.parameters.count(3) }
    }

}