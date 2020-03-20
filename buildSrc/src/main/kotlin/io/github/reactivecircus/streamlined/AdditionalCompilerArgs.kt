package io.github.reactivecircus.streamlined

internal val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-XXLanguage:+InlineClasses",
    "-Xopt-in=kotlin.Experimental"
)
