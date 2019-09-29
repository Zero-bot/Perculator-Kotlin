package perculator

import main.affirm.AffirmationBuilderFactory
import main.affirm.rule.Condition
import main.affirm.rule.Rule
import main.http.Http
import main.http.Parameters
import main.result.Action
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
    fun testHttpParametersOperations(){
        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        `when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        var http = Http(this.httpServletRequest)
        assertTrue { http.parameters.hasKey("name") }
        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        var ruleSuccessCheck = Rule(arrayOf(matchCondition, hasKeyCondition), Action.Allow, Action.Reject)

        matchCondition.value = "\\d"

        var ruleFailCheck = Rule(arrayOf(matchCondition, hasKeyCondition), Action.Allow, Action.Reject)

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, arrayOf(ruleSuccessCheck, ruleFailCheck)).affirm())

    }

}