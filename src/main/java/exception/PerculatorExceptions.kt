package exception

class InvalidNameException(message: String) : Exception(message)
class NoSuchParameterException(name: String): Exception("Parameter $name doesn't exist in given context.")
class ValueCannotBeNullException(operator: String): Exception("Value cannot be null for $operator operator.")
class KeyCannotBeNullException(operator: String): Exception("Key cannot be null for $operator operator.")
class OperatorNotSupportedException(keyword: Byte, location: String): Exception("Operator byte $keyword not supported for $location")
class InvalidLocationException(location: Byte): Exception("Location $location is not a valid location.")
class NoSuchCookieException(key: String): Exception("No such cookie named $key. Always confirm presence of cookie before validating its value")
class NoSuchHeaderException(key: String): Exception("No such header named $key. Always confirm presence of header before validating its value")
