package io.perculator.core.exception

public sealed class PerculatorException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

public class RuleLoadException(message: String, cause: Throwable? = null) : PerculatorException(message, cause)

public class EvaluationException(message: String, cause: Throwable? = null) : PerculatorException(message, cause)

public class InvalidConditionException(message: String) : PerculatorException(message)

public class RequestBodyTooLargeException(limit: Int) : PerculatorException("Request body exceeds $limit bytes")
