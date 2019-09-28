package main.exception

class InvalidNameException(message: String) : Exception(message)
class NoSuchParameterException(name: String): Exception("Parameter $name doesn't exist in given context.")
