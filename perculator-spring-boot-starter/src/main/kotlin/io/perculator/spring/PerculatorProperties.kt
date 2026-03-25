package io.perculator.spring

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "perculator")
public data class PerculatorProperties(
    val enabled: Boolean = true,
    val rulesPath: String = "classpath:perculator-rules.json",
    val bodyMaxSize: Int = 512 * 1024,
    val filterOrder: Int = -2147483638,
    val urlPatterns: List<String> = listOf("/*"),
)
