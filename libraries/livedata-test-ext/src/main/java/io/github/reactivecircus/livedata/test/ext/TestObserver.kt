package io.github.reactivecircus.livedata.test.ext

import androidx.annotation.IntRange
import androidx.lifecycle.Observer

/**
 * An implementation of [Observer] that reports the recorded values emitted by the LiveData.
 */
@ExperimentalStdlibApi
class TestObserver<T> : Observer<T> {

    private val values = mutableListOf<T>()

    /**
     * Takes the first [numberOfValues] recorded values emitted by the LiveData.
     */
    fun take(@IntRange(from = 1) numberOfValues: Int): List<T> {
        require(numberOfValues > 0) {
            "Least number of values to take is 1."
        }
        require(numberOfValues <= values.size) {
            "Taking $numberOfValues but only ${values.size} value(s) have been recorded."
        }
        val drainedValues = mutableListOf<T>()
        while (drainedValues.size < numberOfValues) {
            drainedValues += values.removeFirst()
        }
        return drainedValues
    }

    /**
     * Takes all recorded values emitted by the LiveData.
     */
    fun takeAll(): List<T> {
        val drainedValues = buildList { addAll(values) }
        values.clear()
        return drainedValues
    }

    /**
     * Clears all recorded values emitted by the LiveData.
     */
    fun reset() {
        values.clear()
    }

    override fun onChanged(value: T) {
        values += value
    }
}
