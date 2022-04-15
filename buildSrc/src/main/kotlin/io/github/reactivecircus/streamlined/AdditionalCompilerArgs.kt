package io.github.reactivecircus.streamlined

internal val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+InlineClasses",
    "-Xjvm-default=all",
    "-opt-in=kotlin.Experimental",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime"
)
