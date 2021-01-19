package io.github.reactivecircus.streamlined

internal val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+InlineClasses",
    "-Xuse-ir",
    "-Xjvm-default=all",
    "-Xopt-in=kotlin.Experimental",
    "-Xopt-in=kotlin.ExperimentalStdlibApi",
    "-Xopt-in=kotlin.time.ExperimentalTime"
)
