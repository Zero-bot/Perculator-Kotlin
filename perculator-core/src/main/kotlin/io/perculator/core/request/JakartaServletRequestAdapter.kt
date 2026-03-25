package io.perculator.core.request

import jakarta.servlet.http.HttpServletRequest

public class JakartaServletRequestAdapter(
    private val request: HttpServletRequest,
    private val cachedBody: String? = null,
) : PerculatorHttpRequest {

    override val method: String get() = request.method
    override val requestUri: String get() = request.requestURI
    override val requestUrl: String get() = request.requestURL.toString()
    override val queryString: String? get() = request.queryString
    override val scheme: String get() = request.scheme
    override val protocol: String get() = request.protocol
    override val serverName: String get() = request.serverName
    override val serverPort: Int get() = request.serverPort

    override fun getParameter(name: String): String? = request.getParameter(name)

    override fun getParameterValues(name: String): Array<String>? = request.getParameterValues(name)

    @Suppress("UNCHECKED_CAST")
    override val parameterMap: Map<String, Array<String>>
        get() = request.parameterMap as Map<String, Array<String>>

    override fun getHeader(name: String): String? = request.getHeader(name)

    override fun getHeaders(name: String): List<String> =
        request.getHeaders(name)?.toList() ?: emptyList()

    override val headerNames: List<String>
        get() = request.headerNames?.toList() ?: emptyList()

    override val cookies: List<PerculatorCookie>
        get() = request.cookies?.map { PerculatorCookie(it.name, it.value) } ?: emptyList()

    override val body: String? get() = cachedBody

    override val contentType: String? get() = request.contentType
}
