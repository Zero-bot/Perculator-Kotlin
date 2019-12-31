package http

import wrappers.MutableHttpServletRequest

class JsonParameters(val httpServletRequest: MutableHttpServletRequest) {
    companion object {
        const val hasParameter: Byte = 1
        const val matchParameter: Byte = 2
        const val hasParameterWithValue: Byte = 3
        const val matchParameterWithValue: Byte = 4
        const val match: Byte = 5
        const val countParameters: Byte = 6
        const val valueHasOnlyAlphaNumericChar: Byte = 7
    }
}