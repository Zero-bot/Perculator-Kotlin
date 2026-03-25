package io.perculator.core.model

import io.perculator.core.exception.InvalidConditionException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class LocationTest {

    @Test
    fun `fromString parses Http-dot notation`() {
        assertThat(Location.fromString("Http.Parameter")).isEqualTo(Location.Parameter)
        assertThat(Location.fromString("Http.Cookie")).isEqualTo(Location.Cookie)
        assertThat(Location.fromString("Http.Header")).isEqualTo(Location.Header)
        assertThat(Location.fromString("Http.Request")).isEqualTo(Location.Request)
    }

    @Test
    fun `fromString parses lowercase notation`() {
        assertThat(Location.fromString("parameter")).isEqualTo(Location.Parameter)
        assertThat(Location.fromString("cookie")).isEqualTo(Location.Cookie)
        assertThat(Location.fromString("header")).isEqualTo(Location.Header)
        assertThat(Location.fromString("request")).isEqualTo(Location.Request)
    }

    @Test
    fun `fromString throws for unknown location`() {
        assertThatThrownBy { Location.fromString("unknown") }
            .isInstanceOf(InvalidConditionException::class.java)
            .hasMessageContaining("Unknown location")
    }
}
