package perculator_json

import affirm.Perculator
import helpers.TestFailureHelpers
import helpers.TestSuccessHelpers
import org.junit.jupiter.api.Test
import result.Action
import kotlin.test.assertEquals

class JsonRulesCheckParameters {

    @Test
    fun testJsonRuleCheckHasParameter(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest
        var perculator = Perculator("src/test/resources/JsonRulesCheckParameters/testJsonRuleCheckHasParameter.json")
        assertEquals(Action.Allow, perculator.evaluate(httpServletRequest))
    }

    @Test
    fun testJsonRuleCheckHasParameterFailure(){
        val httpServletRequest = TestFailureHelpers().httpServletRequest
        var perculator = Perculator("src/test/resources/JsonRulesCheckParameters/testJsonRuleCheckHasParameterFailure.json")
        assertEquals(Action.Reject, perculator.evaluate(httpServletRequest))
    }

    @Test
    fun testJsonRuleCheckMatchParameter(){
        val httpServletRequest = TestSuccessHelpers().httpServletRequest
        var perculator = Perculator("src/test/resources/JsonRulesCheckParameters/testJsonRuleCheckMatchParameter.json")
        assertEquals(Action.Allow, perculator.evaluate(httpServletRequest))
    }

}