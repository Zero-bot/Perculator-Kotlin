package io.perculator.core

import java.nio.file.Path

public data class PerculatorConfig(
    val rulesPath: Path,
    val maxBodySize: Int = 512 * 1024,
    val watchRules: Boolean = true,
)
