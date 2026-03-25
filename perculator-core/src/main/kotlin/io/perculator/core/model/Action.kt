package io.perculator.core.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

public enum class Action(public val priority: Int) {
    REJECT(0),
    REDIRECT(1),
    LOG(2),
    ALLOW(3),
    SKIP(4);

    @JsonValue
    public fun toJson(): String = name.lowercase().replaceFirstChar { it.uppercase() }

    public companion object {
        @JvmStatic
        @JsonCreator
        public fun fromString(value: String): Action = when (value.lowercase()) {
            "reject" -> REJECT
            "redirect" -> REDIRECT
            "log" -> LOG
            "allow" -> ALLOW
            "skip" -> SKIP
            else -> throw IllegalArgumentException("Unknown action: $value")
        }
    }
}
