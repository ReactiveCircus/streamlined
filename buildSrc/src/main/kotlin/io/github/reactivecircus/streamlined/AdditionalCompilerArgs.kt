package io.github.reactivecircus.streamlined

internal val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-Xuse-experimental=kotlin.Experimental"
)
