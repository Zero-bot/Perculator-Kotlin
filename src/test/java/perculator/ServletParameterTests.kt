package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import http.Http
import http.Parameters
import result.Action
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.servlet.http.HttpServletRequest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServletTests {
    lateinit var httpServletRequest: HttpServletRequest

    @Test
    fun testHttpParametersOperations(){
        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        val parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")
        `when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        var http = Http(this.httpServletRequest)
        assertTrue { http.parameters.hasKey("name") }

        var contextCondition = Condition(Http.Parameter, Parameters.hasKey, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchValue, "^password$", "\\W")
        var ruleFailCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "password")
        var ruleAlphanumericCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "other")
        var ruleAlphanumericFailCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = arrayOf(
            ruleSuccessCheck,
            ruleFailCheck,
            ruleAlphanumericCheck,
            ruleAlphanumericFailCheck
        )

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }

    @Test
    fun testContextFailHttpParametersOperations(){
        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("password"))
        parameters["other"] = arrayOf("first", "\\//")
        `when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        var http = Http(this.httpServletRequest)
        assertTrue { http.parameters.hasKey("name") }

        var contextConditionFail = Condition(Http.Parameter, Parameters.hasKey, "passwords")
        var contextCondition = Condition(Http.Parameter, Parameters.hasKey, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchValue, "^password$", "\\W")
        var ruleFailCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "password")
        var ruleAlphanumericCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "other")
        var ruleAlphanumericFailCheck = Rule(
            arrayOf(contextConditionFail),
            arrayOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = arrayOf(
            ruleSuccessCheck,
            ruleFailCheck,
            ruleAlphanumericCheck,
            ruleAlphanumericFailCheck
        )

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }

    @Test
    fun testContextFailHttpParametersOperationsReject(){
        this.httpServletRequest = Mockito.mock(HttpServletRequest::class.java)
        var parameters = mutableMapOf("name" to arrayOf("Marimuthu Mahalingam"), "password" to arrayOf("&^%$"))
        parameters["other"] = arrayOf("first", "\\//")
        `when`(httpServletRequest.parameterMap).thenReturn(parameters as Map<*, *>?)
        var http = Http(this.httpServletRequest)
        assertTrue { http.parameters.hasKey("name") }

        var contextConditionFail = Condition(Http.Parameter, Parameters.hasKey, "passwords")
        var contextCondition = Condition(Http.Parameter, Parameters.hasKey, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasKey, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasKey, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchValue, "^password$", "\\W")
        var ruleFailCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "password")
        var ruleAlphanumericCheck = Rule(
            arrayOf(contextCondition),
            arrayOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.hasOnlyAlphaNumberic, "other")
        var ruleAlphanumericFailCheck = Rule(
            arrayOf(contextConditionFail),
            arrayOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = arrayOf(
            ruleSuccessCheck,
            ruleFailCheck,
            ruleAlphanumericCheck,
            ruleAlphanumericFailCheck
        )

        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }



}