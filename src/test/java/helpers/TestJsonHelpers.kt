package helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import wrappers.MutableHttpServletRequest
import java.util.*

@ImplicitReflectionSerializer
class TestJsonHelpers {
    var httpServletRequest: MutableHttpServletRequest
    init {
        val body = mutableMapOf<String, String>()
        body["password"] = "password"
        body["username"] = "Marimuthu"
        val json = Json(JsonConfiguration.Stable)
        val jsonData = json.stringify(body)
        this.httpServletRequest = Mockito.mock(MutableHttpServletRequest::class.java)
        Mockito.`when`(httpServletRequest.getBody()).thenReturn(jsonData)
        Mockito.`when`(httpServletRequest.getHeader("Content-Type")).thenReturn("application/json")
        Mockito.`when`(httpServletRequest.contentType).thenReturn("application/json")
        Mockito.`when`(httpServletRequest.headerNames).thenReturn(listOf<String>("content-type").toEnumeration())
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