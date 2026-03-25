package io.perculator.core.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ActionTest {

    @Test
    fun `REJECT has highest priority`() {
        assertThat(Action.REJECT.priority).isLessThan(Action.ALLOW.priority)
        assertThat(Action.REJECT.priority).isLessThan(Action.SKIP.priority)
    }

    @Test
    fun `priority ordering is REJECT, REDIRECT, LOG, ALLOW, SKIP`() {
        val sorted = Action.entries.sortedBy { it.priority }
        assertThat(sorted).containsExactly(
            Action.REJECT, Action.REDIRECT, Action.LOG, Action.ALLOW, Action.SKIP
        )
    }

    @Test
    fun `fromString parses case-insensitively`() {
        assertThat(Action.fromString("Allow")).isEqualTo(Action.ALLOW)
        assertThat(Action.fromString("reject")).isEqualTo(Action.REJECT)
        assertThat(Action.fromString("SKIP")).isEqualTo(Action.SKIP)
    }

    @Test
    fun `fromString throws for unknown`() {
        assertThatThrownBy { Action.fromString("unknown") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `toJson produces capitalized form`() {
        assertThat(Action.ALLOW.toJson()).isEqualTo("Allow")
        assertThat(Action.REJECT.toJson()).isEqualTo("Reject")
    }
}
