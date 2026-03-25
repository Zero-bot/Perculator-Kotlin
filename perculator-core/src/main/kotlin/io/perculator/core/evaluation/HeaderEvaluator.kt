package io.perculator.core.evaluation

import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Operator.HeaderOp
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache

public class HeaderEvaluator(private val regexCache: RegexCache = RegexCache()) : ConditionEvaluator {

    override fun evaluate(condition: Condition, request: PerculatorHttpRequest): Boolean {
        val op = condition.operator as HeaderOp
        val key = condition.key
        val value = condition.value

        return when (op) {
            is HeaderOp.HasHeader -> {
                requireKey(key, "hasHeader")
                request.headerNames.any { it.equals(key, ignoreCase = true) }
            }
            is HeaderOp.HeaderHasValue -> {
                requireKey(key, "headerHasValue")
                requireValue(value, "headerHasValue")
                ensureHeaderExists(request, key!!)
                request.getHeaders(key).any { it == value }
            }
            is HeaderOp.MatchHeader -> {
                requireKey(key, "matchHeader")
                val regex = regexCache.get(key!!, setOf(RegexOption.IGNORE_CASE))
                request.headerNames.any { regex.containsMatchIn(it) }
            }
            is HeaderOp.MatchHeaderValue -> {
                requireKey(key, "matchHeaderValue")
                requireValue(value, "matchHeaderValue")
                ensureHeaderExists(request, key!!)
                val regex = regexCache.get(value!!)
                request.getHeaders(key).any { regex.containsMatchIn(it) }
            }
            is HeaderOp.IsHeaderRepeated -> {
                requireKey(key, "isHeaderRepeated")
                request.getHeaders(key!!).size > 1
            }
            is HeaderOp.HeaderHasSpecialChar -> {
                requireKey(key, "headerHasSpecialChar")
                ensureHeaderExists(request, key!!)
                val regex = regexCache.get("[\\W\\s_]")
                request.getHeaders(key).any { regex.containsMatchIn(it) }
            }
            is HeaderOp.TotalHeadersCountEquals -> {
                requireKey(key, "totalHeadersCountEquals")
                val count = key!!.toIntOrNull() ?: throw EvaluationException("Invalid count: $key")
                val totalCount = request.headerNames.sumOf { request.getHeaders(it).size }
                totalCount == count
            }
            is HeaderOp.HeaderCountEquals -> {
                requireKey(key, "headerCountEquals")
                requireValue(value, "headerCountEquals")
                val count = value!!.toIntOrNull() ?: throw EvaluationException("Invalid count: $value")
                request.getHeaders(key!!).size == count
            }
            is HeaderOp.MatchAllHeadersValue -> {
                requireKey(key, "matchAllHeadersValue")
                requireValue(value, "matchAllHeadersValue")
                ensureHeaderExists(request, key!!)
                val regex = regexCache.get(value!!)
                request.getHeaders(key).all { regex.containsMatchIn(it) }
            }
        }
    }

    private fun ensureHeaderExists(request: PerculatorHttpRequest, key: String) {
        if (!request.headerNames.any { it.equals(key, ignoreCase = true) }) {
            throw EvaluationException("No such header: $key")
        }
    }

    private fun requireKey(key: String?, operatorName: String) {
        if (key == null) throw EvaluationException("Key cannot be null for $operatorName")
    }

    private fun requireValue(value: String?, operatorName: String) {
        if (value == null) throw EvaluationException("Value cannot be null for $operatorName")
    }
}
