package io.perculator.core.util

import java.util.concurrent.ConcurrentHashMap

public class RegexCache(private val maxSize: Int = 256) {
    private val cache = ConcurrentHashMap<String, Regex>()

    public fun get(pattern: String): Regex =
        cache.computeIfAbsent(pattern) { it.toRegex() }

    public fun get(pattern: String, options: Set<RegexOption>): Regex {
        val key = "$pattern|${options.joinToString(",")}"
        return cache.computeIfAbsent(key) { pattern.toRegex(options) }
    }

    public fun size(): Int = cache.size

    public fun clear() {
        cache.clear()
    }
}
