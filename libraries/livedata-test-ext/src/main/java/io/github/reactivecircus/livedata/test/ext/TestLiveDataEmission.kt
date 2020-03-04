import androidx.lifecycle.Observer

/**
 * Invokes `onChanged(value)` on [Observer] with each value passed in.
 * This is useful when verifying the emitted values from a mocked [Observer] in tests.
 */
fun <T> Observer<T>.emitted(vararg values: T) {
    values.forEach { value ->
        onChanged(value)
    }
}
