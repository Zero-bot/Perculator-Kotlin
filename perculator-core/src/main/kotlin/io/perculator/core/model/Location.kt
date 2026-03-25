package io.perculator.core.model

import io.perculator.core.exception.InvalidConditionException

public sealed class Location {
    public data object Parameter : Location()
    public data object Cookie : Location()
    public data object Header : Location()
    public data object Request : Location()

    public companion object {
        public fun fromString(value: String): Location = when (value) {
            "Http.Parameter", "parameter" -> Parameter
            "Http.Cookie", "cookie" -> Cookie
            "Http.Header", "header" -> Header
            "Http.Request", "request" -> Request
            else -> throw InvalidConditionException("Unknown location: $value")
        }
    }
}
