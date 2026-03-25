package io.perculator.core.evaluation

import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Operator.RequestOp
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache

public class RequestEvaluator(private val regexCache: RegexCache = RegexCache()) : ConditionEvaluator {

    override fun evaluate(condition: Condition, request: PerculatorHttpRequest): Boolean {
        val op = condition.operator as RequestOp
        val key = condition.key

        return when (op) {
            is RequestOp.MethodEquals -> {
                requireKey(key, "methodEquals")
                key.equals(request.method, ignoreCase = true)
            }
            is RequestOp.MethodNotEquals -> {
                requireKey(key, "methodNotEquals")
                !key.equals(request.method, ignoreCase = true)
            }
            is RequestOp.IsGetRequest -> "GET".equals(request.method, ignoreCase = true)
            is RequestOp.IsPostRequest -> "POST".equals(request.method, ignoreCase = true)
            is RequestOp.IsHeadRequest -> "HEAD".equals(request.method, ignoreCase = true)
            is RequestOp.IsPutRequest -> "PUT".equals(request.method, ignoreCase = true)
            is RequestOp.IsDeleteRequest -> "DELETE".equals(request.method, ignoreCase = true)
            is RequestOp.IsConnectRequest -> "CONNECT".equals(request.method, ignoreCase = true)
            is RequestOp.IsOptionsRequest -> "OPTIONS".equals(request.method, ignoreCase = true)
            is RequestOp.IsTraceRequest -> "TRACE".equals(request.method, ignoreCase = true)
            is RequestOp.IsPatchRequest -> "PATCH".equals(request.method, ignoreCase = true)

            is RequestOp.QueryStringEquals -> {
                requireKey(key, "queryStringEquals")
                key == request.queryString
            }
            is RequestOp.QueryStringNotEquals -> {
                requireKey(key, "queryStringNotEquals")
                key != request.queryString
            }
            is RequestOp.QueryStringContains -> {
                requireKey(key, "queryStringContains")
                val qs = request.queryString
                    ?: throw EvaluationException("Query string is null")
                qs.contains(key!!)
            }
            is RequestOp.QueryStringNotContains -> {
                requireKey(key, "queryStringNotContains")
                val qs = request.queryString
                    ?: throw EvaluationException("Query string is null")
                !qs.contains(key!!)
            }
            is RequestOp.QueryStringMatch -> {
                requireKey(key, "queryStringMatch")
                val qs = request.queryString
                    ?: throw EvaluationException("Query string is null")
                regexCache.get(key!!).containsMatchIn(qs)
            }
            is RequestOp.QueryStringNotMatch -> {
                requireKey(key, "queryStringNotMatch")
                val qs = request.queryString
                    ?: throw EvaluationException("Query string is null")
                !regexCache.get(key!!).containsMatchIn(qs)
            }

            is RequestOp.UriEquals -> { requireKey(key, "uriEquals"); request.requestUri == key }
            is RequestOp.UriNotEquals -> { requireKey(key, "uriNotEquals"); request.requestUri != key }
            is RequestOp.UriContains -> { requireKey(key, "uriContains"); request.requestUri.contains(key!!) }
            is RequestOp.UriNotContains -> { requireKey(key, "uriNotContains"); !request.requestUri.contains(key!!) }
            is RequestOp.UriMatch -> { requireKey(key, "uriMatch"); regexCache.get(key!!).containsMatchIn(request.requestUri) }
            is RequestOp.UriNotMatch -> { requireKey(key, "uriNotMatch"); !regexCache.get(key!!).containsMatchIn(request.requestUri) }

            is RequestOp.SchemeEquals -> { requireKey(key, "schemeEquals"); key == request.scheme }
            is RequestOp.SchemeNotEquals -> { requireKey(key, "schemeNotEquals"); key != request.scheme }

            is RequestOp.ProtocolEquals -> { requireKey(key, "protocolEquals"); request.protocol == key }
            is RequestOp.ProtocolNotEquals -> { requireKey(key, "protocolNotEquals"); request.protocol != key }
            is RequestOp.ProtocolMatch -> { requireKey(key, "protocolMatch"); regexCache.get(key!!).containsMatchIn(request.protocol) }
            is RequestOp.ProtocolNotMatch -> { requireKey(key, "protocolNotMatch"); !regexCache.get(key!!).containsMatchIn(request.protocol) }

            is RequestOp.UrlEquals -> { requireKey(key, "urlEquals"); request.requestUrl == key }
            is RequestOp.UrlNotEquals -> { requireKey(key, "urlNotEquals"); request.requestUrl != key }
            is RequestOp.UrlMatch -> { requireKey(key, "urlMatch"); regexCache.get(key!!).containsMatchIn(request.requestUrl) }
            is RequestOp.UrlNotMatch -> { requireKey(key, "urlNotMatch"); !regexCache.get(key!!).containsMatchIn(request.requestUrl) }
            is RequestOp.UrlContains -> { requireKey(key, "urlContains"); request.requestUrl.contains(key!!) }
            is RequestOp.UrlNotContains -> { requireKey(key, "urlNotContains"); !request.requestUrl.contains(key!!) }

            is RequestOp.ServerEquals -> { requireKey(key, "serverEquals"); request.serverName == key }
            is RequestOp.ServerNotEquals -> { requireKey(key, "serverNotEquals"); request.serverName != key }
            is RequestOp.ServerMatch -> { requireKey(key, "serverMatch"); regexCache.get(key!!).containsMatchIn(request.serverName) }
            is RequestOp.ServerNotMatch -> { requireKey(key, "serverNotMatch"); !regexCache.get(key!!).containsMatchIn(request.serverName) }

            is RequestOp.PortEquals -> {
                requireKey(key, "portEquals")
                val port = key!!.toIntOrNull() ?: throw EvaluationException("Invalid port: $key")
                request.serverPort == port
            }
            is RequestOp.PortNotEquals -> {
                requireKey(key, "portNotEquals")
                val port = key!!.toIntOrNull() ?: throw EvaluationException("Invalid port: $key")
                request.serverPort != port
            }
        }
    }

    private fun requireKey(key: String?, operatorName: String) {
        if (key == null) throw EvaluationException("Key cannot be null for $operatorName")
    }
}
