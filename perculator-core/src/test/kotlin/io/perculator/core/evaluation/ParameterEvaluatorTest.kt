package io.perculator.core.evaluation

import io.mockk.every
import io.mockk.mockk
import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Location
import io.perculator.core.model.Operator.ParameterOp
import io.perculator.core.request.PerculatorHttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ParameterEvaluatorTest {

    private val evaluator = ParameterEvaluator()

    private fun mockRequest(params: Map<String, Array<String>> = emptyMap()): PerculatorHttpRequest {
        val request = mockk<PerculatorHttpRequest>()
        every { request.parameterMap } returns params
        return request
    }

    @Test
    fun `hasParameter returns true when parameter exists`() {
        val request = mockRequest(mapOf("name" to arrayOf("john")))
        val condition = Condition(Location.Parameter, ParameterOp.HasParameter, key = "name")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `hasParameter returns false when parameter missing`() {
        val request = mockRequest(emptyMap())
        val condition = Condition(Location.Parameter, ParameterOp.HasParameter, key = "name")
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `matchParameter matches regex on keys`() {
        val request = mockRequest(mapOf("username" to arrayOf("john")))
        val condition = Condition(Location.Parameter, ParameterOp.MatchParameter, key = "user.*")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `hasParameterWithValue checks exact value`() {
        val request = mockRequest(mapOf("name" to arrayOf("john")))
        val condition = Condition(Location.Parameter, ParameterOp.HasParameterWithValue, key = "name", value = "john")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `hasParameterWithValue returns false for wrong value`() {
        val request = mockRequest(mapOf("name" to arrayOf("jane")))
        val condition = Condition(Location.Parameter, ParameterOp.HasParameterWithValue, key = "name", value = "john")
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `match validates regex on values`() {
        val request = mockRequest(mapOf("password" to arrayOf("pass123")))
        val condition = Condition(Location.Parameter, ParameterOp.Match, key = "password", value = "pass\\d+")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `countParameters checks count`() {
        val request = mockRequest(mapOf("a" to arrayOf("1"), "b" to arrayOf("2")))
        val condition = Condition(Location.Parameter, ParameterOp.CountParameters, key = "2")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `valueHasOnlyAlphaNumericChar validates alphanumeric`() {
        val request = mockRequest(mapOf("name" to arrayOf("john123")))
        val condition = Condition(Location.Parameter, ParameterOp.ValueHasOnlyAlphaNumericChar, key = "name")
        assertThat(evaluator.evaluate(condition, request)).isTrue()
    }

    @Test
    fun `valueHasOnlyAlphaNumericChar rejects special chars`() {
        val request = mockRequest(mapOf("name" to arrayOf("john!@#")))
        val condition = Condition(Location.Parameter, ParameterOp.ValueHasOnlyAlphaNumericChar, key = "name")
        assertThat(evaluator.evaluate(condition, request)).isFalse()
    }

    @Test
    fun `null key throws EvaluationException`() {
        val request = mockRequest()
        val condition = Condition(Location.Parameter, ParameterOp.HasParameter, key = null)
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("Key cannot be null")
    }

    @Test
    fun `null value throws for operators requiring value`() {
        val request = mockRequest(mapOf("name" to arrayOf("john")))
        val condition = Condition(Location.Parameter, ParameterOp.HasParameterWithValue, key = "name", value = null)
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("Value cannot be null")
    }

    @Test
    fun `missing parameter throws for hasParameterWithValue`() {
        val request = mockRequest(emptyMap())
        val condition = Condition(Location.Parameter, ParameterOp.HasParameterWithValue, key = "missing", value = "val")
        assertThatThrownBy { evaluator.evaluate(condition, request) }
            .isInstanceOf(EvaluationException::class.java)
            .hasMessageContaining("No such parameter")
    }
}
