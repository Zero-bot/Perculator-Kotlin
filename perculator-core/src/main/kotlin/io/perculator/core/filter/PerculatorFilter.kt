package io.perculator.core.filter

import io.perculator.core.Perculator
import io.perculator.core.PerculatorConfig
import io.perculator.core.model.Action
import io.perculator.core.request.CachingRequestWrapper
import io.perculator.core.request.JakartaServletRequestAdapter
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import java.nio.file.Paths

public class PerculatorFilter : Filter {

    private val logger = LoggerFactory.getLogger(PerculatorFilter::class.java)
    private var perculator: Perculator? = null
    private var maxBodySize: Int = CachingRequestWrapper.DEFAULT_MAX_BODY_SIZE
    private var ownsPerculator = false

    public constructor(perculator: Perculator) {
        this.perculator = perculator
    }

    public constructor()

    override fun init(filterConfig: FilterConfig) {
        if (perculator == null) {
            val rulesPath = filterConfig.getInitParameter("perculator.rules.path")
                ?: throw ServletException("perculator.rules.path init parameter is required")
            val bodySizeParam = filterConfig.getInitParameter("perculator.body.maxSize")
            if (bodySizeParam != null) {
                maxBodySize = bodySizeParam.toIntOrNull()
                    ?: throw ServletException("Invalid perculator.body.maxSize: $bodySizeParam")
            }
            val config = PerculatorConfig(
                rulesPath = Paths.get(rulesPath),
                maxBodySize = maxBodySize,
            )
            perculator = Perculator.create(config)
            ownsPerculator = true
        }
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request !is HttpServletRequest || response !is HttpServletResponse) {
            chain.doFilter(request, response)
            return
        }

        val wrappedRequest = CachingRequestWrapper(request, maxBodySize)
        val adapted = JakartaServletRequestAdapter(wrappedRequest, wrappedRequest.getBody())
        val result = perculator!!.evaluate(adapted)

        when (result.action) {
            Action.REJECT -> {
                logger.warn("Request rejected by rule '{}': {} {}", result.ruleName, request.method, request.requestURI)
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")
            }
            Action.REDIRECT -> {
                logger.warn("Request redirected by rule '{}': {} {}", result.ruleName, request.method, request.requestURI)
                response.sendError(HttpServletResponse.SC_FORBIDDEN)
            }
            Action.LOG -> {
                logger.warn("Rule '{}' flagged request: {} {}", result.ruleName, request.method, request.requestURI)
                chain.doFilter(wrappedRequest, response)
            }
            Action.ALLOW, Action.SKIP -> {
                chain.doFilter(wrappedRequest, response)
            }
        }
    }

    override fun destroy() {
        if (ownsPerculator) {
            perculator?.close()
        }
    }
}
