package io.perculator.core.util

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class RegexCacheTest {

    @Test
    fun `caches regex instances`() {
        val cache = RegexCache()
        val first = cache.get("test.*")
        val second = cache.get("test.*")
        assertThat(first).isSameAs(second)
    }

    @Test
    fun `different patterns produce different instances`() {
        val cache = RegexCache()
        val a = cache.get("a.*")
        val b = cache.get("b.*")
        assertThat(a).isNotSameAs(b)
    }

    @Test
    fun `supports regex options`() {
        val cache = RegexCache()
        val caseInsensitive = cache.get("test", setOf(RegexOption.IGNORE_CASE))
        assertThat(caseInsensitive.containsMatchIn("TEST")).isTrue()
    }

    @Test
    fun `invalid regex throws PatternSyntaxException`() {
        val cache = RegexCache()
        assertThatThrownBy { cache.get("[invalid") }
            .isInstanceOf(java.util.regex.PatternSyntaxException::class.java)
    }

    @Test
    fun `thread safe concurrent access`() {
        val cache = RegexCache()
        val threads = 10
        val executor = Executors.newFixedThreadPool(threads)
        val latch = CountDownLatch(threads)
        val results = mutableListOf<Regex>()

        repeat(threads) {
            executor.submit {
                try {
                    results.add(cache.get("shared.*pattern"))
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executor.shutdown()
        assertThat(results).hasSize(threads)
        assertThat(results.distinct()).hasSize(1)
    }

    @Test
    fun `size tracks cached entries`() {
        val cache = RegexCache()
        assertThat(cache.size()).isEqualTo(0)
        cache.get("a")
        cache.get("b")
        assertThat(cache.size()).isEqualTo(2)
    }

    @Test
    fun `clear removes all entries`() {
        val cache = RegexCache()
        cache.get("a")
        cache.get("b")
        cache.clear()
        assertThat(cache.size()).isEqualTo(0)
    }
}
