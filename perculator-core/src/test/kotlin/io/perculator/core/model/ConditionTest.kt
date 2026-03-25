package io.perculator.core.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConditionTest {

    @Test
    fun `condition with all fields`() {
        val condition = Condition(
            location = Location.Parameter,
            operator = Operator.ParameterOp.HasParameter,
            key = "name",
            value = "john",
        )
        assertThat(condition.location).isEqualTo(Location.Parameter)
        assertThat(condition.operator).isEqualTo(Operator.ParameterOp.HasParameter)
        assertThat(condition.key).isEqualTo("name")
        assertThat(condition.value).isEqualTo("john")
    }

    @Test
    fun `condition with null key and value`() {
        val condition = Condition(
            location = Location.Request,
            operator = Operator.RequestOp.IsGetRequest,
        )
        assertThat(condition.key).isNull()
        assertThat(condition.value).isNull()
    }

    @Test
    fun `operator fromString maps correctly`() {
        assertThat(Operator.fromString(Location.Parameter, "Parameter.hasParameter"))
            .isEqualTo(Operator.ParameterOp.HasParameter)
        assertThat(Operator.fromString(Location.Cookie, "Cookie.hasCookie"))
            .isEqualTo(Operator.CookieOp.HasCookie)
        assertThat(Operator.fromString(Location.Header, "Header.hasHeader"))
            .isEqualTo(Operator.HeaderOp.HasHeader)
        assertThat(Operator.fromString(Location.Request, "Request.isGetRequest"))
            .isEqualTo(Operator.RequestOp.IsGetRequest)
    }
}
