package serializer

import kotlinx.serialization.Serializable
import result.Action

@Serializable
data class JsonAction(val success: Action, val failure: Action)