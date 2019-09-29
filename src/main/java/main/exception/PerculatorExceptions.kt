package main.exception

class InvalidNameException(message: String) : Exception(message)
class NoSuchParameterException(name: String): Exception("Parameter $name doesn't exist in given context.")
class ValueCannotBeNullException(operator: String): Exception("Value cannot be null for $operator operator.")
class OperatorNotSupportedException(keyword: Byte, location: String): Exception("Operator byte $keyword not supported for $location")
class InvalidLocationException(location: Byte): Exception("Location $location is not a valid location.")
