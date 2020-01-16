package io.github.reactivecircus.coroutines.test.ext

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.function.ThrowingRunnable

suspend inline fun <reified T : Throwable> assertThrows(
    crossinline runnable: suspend () -> Unit
): T {
    val throwingRunnable = ThrowingRunnable {
        runBlocking { runnable() }
    }
    return Assert.assertThrows(null, T::class.java, throwingRunnable)
}
