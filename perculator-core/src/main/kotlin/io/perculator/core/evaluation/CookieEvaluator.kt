package io.perculator.core.evaluation

import io.perculator.core.exception.EvaluationException
import io.perculator.core.model.Condition
import io.perculator.core.model.Operator.CookieOp
import io.perculator.core.request.PerculatorCookie
import io.perculator.core.request.PerculatorHttpRequest
import io.perculator.core.util.RegexCache

public class CookieEvaluator(private val regexCache: RegexCache = RegexCache()) : ConditionEvaluator {

    override fun evaluate(condition: Condition, request: PerculatorHttpRequest): Boolean {
        val op = condition.operator as CookieOp
        val key = condition.key
        val value = condition.value
        val cookies = request.cookies

        return when (op) {
            is CookieOp.HasCookie -> {
                requireKey(key, "hasCookie")
                cookies.any { it.name == key }
            }
            is CookieOp.CookieHasValue -> {
                requireKey(key, "cookieHasValue")
                requireValue(value, "cookieHasValue")
                val cookie = findCookie(cookies, key!!)
                cookie.value == value
            }
            is CookieOp.MatchCookie -> {
                requireKey(key, "matchCookie")
                val regex = regexCache.get(key!!)
                cookies.any { regex.containsMatchIn(it.name) }
            }
            is CookieOp.MatchValue -> {
                requireKey(key, "matchValue")
                requireValue(value, "matchValue")
                val cookie = findCookie(cookies, key!!)
                val regex = regexCache.get(value!!)
                regex.containsMatchIn(cookie.value)
            }
            is CookieOp.CountCookies -> {
                requireKey(key, "countCookies")
                val count = key!!.toIntOrNull() ?: throw EvaluationException("Invalid count: $key")
                cookies.size == count
            }
            is CookieOp.ValueHasOnlyAlphaNumericChar -> {
                requireKey(key, "valueHasOnlyAlphaNumericChar")
                val cookie = findCookie(cookies, key!!)
                val regex = regexCache.get("^[a-zA-Z0-9]+$")
                regex.containsMatchIn(cookie.value)
            }
            is CookieOp.ValueLengthEquals -> {
                requireKey(key, "valueLengthEquals")
                requireValue(value, "valueLengthEquals")
                val cookie = findCookie(cookies, key!!)
                val length = value!!.toIntOrNull() ?: throw EvaluationException("Invalid length: $value")
                cookie.value.length == length
            }
        }
    }

    private fun findCookie(cookies: List<PerculatorCookie>, name: String): PerculatorCookie =
        cookies.firstOrNull { it.name == name }
            ?: throw EvaluationException("No such cookie: $name")

    private fun requireKey(key: String?, operatorName: String) {
        if (key == null) throw EvaluationException("Key cannot be null for $operatorName")
    }

    private fun requireValue(value: String?, operatorName: String) {
        if (value == null) throw EvaluationException("Value cannot be null for $operatorName")
    }
}
