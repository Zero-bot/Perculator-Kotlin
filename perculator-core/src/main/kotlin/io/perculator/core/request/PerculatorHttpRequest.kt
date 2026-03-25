package io.perculator.core.request

public interface PerculatorHttpRequest {
    public val method: String
    public val requestUri: String
    public val requestUrl: String
    public val queryString: String?
    public val scheme: String
    public val protocol: String
    public val serverName: String
    public val serverPort: Int
    public fun getParameter(name: String): String?
    public fun getParameterValues(name: String): Array<String>?
    public val parameterMap: Map<String, Array<String>>
    public fun getHeader(name: String): String?
    public fun getHeaders(name: String): List<String>
    public val headerNames: List<String>
    public val cookies: List<PerculatorCookie>
    public val body: String?
    public val contentType: String?
}

public data class PerculatorCookie(
    val name: String,
    val value: String,
)
