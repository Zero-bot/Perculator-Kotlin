package perculator

import affirm.AffirmationBuilderFactory
import affirm.rule.Condition
import affirm.rule.Rule
import helpers.TestSuccessHelpers
import http.Http
import http.Parameters
import result.Action
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ServletParameterTests {

    @Test
    fun testHttpParametersOperations(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest

        var contextCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            listOf(contextCondition),
            listOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "^password$", "\\W")
        var ruleFailCheck = Rule(
            listOf(contextCondition),
            listOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "password")
        var ruleAlphanumericCheck = Rule(
            listOf(contextCondition),
            listOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "other")
        var ruleAlphanumericFailCheck = Rule(
            listOf(contextCondition),
            listOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = mapOf(
            "ruleSuccessCheck" to ruleSuccessCheck,
            "ruleFailCheck" to ruleFailCheck,
            "ruleAlphanumericCheck" to ruleAlphanumericCheck,
            "ruleAlphanumericFailCheck" to ruleAlphanumericFailCheck
        )

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }

    @Test
    fun testContextFailHttpParametersOperations(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest

        var contextConditionFail = Condition(Http.Parameter, Parameters.hasParameter, "passwords")
        var contextCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            listOf(contextCondition),
            listOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "^password$", "\\W")
        var ruleFailCheck = Rule(
            listOf(contextCondition),
            listOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "password")
        var ruleAlphanumericCheck = Rule(
            listOf(contextCondition),
            listOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "other")
        var ruleAlphanumericFailCheck = Rule(
            listOf(contextConditionFail),
            listOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = mapOf(
            "ruleSuccessCheck" to ruleSuccessCheck,
            "ruleFailCheck" to ruleFailCheck,
            "ruleAlphanumericCheck" to ruleAlphanumericCheck,
            "ruleAlphanumericFailCheck" to ruleAlphanumericFailCheck
        )

        assertEquals(Action.Allow, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }

    @Test
    fun testContextFailHttpParametersOperationsReject(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest

        var contextConditionFail = Condition(Http.Parameter, Parameters.hasParameter, "passwords")
        var contextCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")

        var matchCondition = Condition(Http.Parameter, Parameters.match, "password", "\\w")
        var hasKeyCondition = Condition(Http.Parameter, Parameters.hasParameter, "name")
        var matchValueCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "n[am]{2}e", "^Mari\\w+\\sMaha\\w+$")
        var ruleSuccessCheck = Rule(
            listOf(contextCondition),
            listOf(matchCondition, hasKeyCondition, matchValueCondition),
            Action.Allow,
            Action.Reject
        )


        var hasPasswordCondition = Condition(Http.Parameter, Parameters.hasParameter, "password")
        var matchPasswordCondition =
            Condition(Http.Parameter, Parameters.matchParameterWithValue, "^password$", "^\\w+$")
        var ruleFailCheck = Rule(
            listOf(contextCondition),
            listOf(hasPasswordCondition, matchPasswordCondition),
            Action.Reject,
            Action.Allow
        )

        var hasAlphanumericCondition =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "password")
        var ruleAlphanumericCheck = Rule(
            listOf(contextCondition),
            listOf(hasAlphanumericCondition),
            Action.Allow,
            Action.Reject
        )

        var hasAlphanumericConditionFail =
            Condition(Http.Parameter, Parameters.valueHasOnlyAlphaNumericChar, "other")
        var ruleAlphanumericFailCheck = Rule(
            listOf(contextConditionFail),
            listOf(hasAlphanumericConditionFail),
            Action.Reject,
            Action.Allow
        )

        val rules = mapOf(
            "ruleSuccessCheck" to ruleSuccessCheck,
            "ruleFailCheck" to ruleFailCheck,
            "ruleAlphanumericCheck" to ruleAlphanumericCheck,
            "ruleAlphanumericFailCheck" to ruleAlphanumericFailCheck
        )

        assertEquals(Action.Reject, AffirmationBuilderFactory(httpServletRequest, rules).affirm())

    }



}