@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.persistence

// SQLite only supports up to 999 parameters in a single statement
internal const val MAX_PARAMETERS_PER_STATEMENT = 999
