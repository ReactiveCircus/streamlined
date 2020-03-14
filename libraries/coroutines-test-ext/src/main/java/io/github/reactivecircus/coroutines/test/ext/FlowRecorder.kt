package io.github.reactivecircus.coroutines.test.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
fun <T> Flow<T>.recordWith(recorder: FlowRecorder<T>) {
    onEach { recorder.values += it }.launchIn(recorder.coroutineScope)
}

@ExperimentalStdlibApi
class FlowRecorder<T>(internal val coroutineScope: CoroutineScope) {

    internal val values = mutableListOf<T>()

    /**
     * Takes the first [numberOfValues] recorded values emitted by the [Flow].
     */
    fun take(numberOfValues: Int): List<T> {
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
     * Takes all recorded values emitted by the [Flow].
     */
    fun takeAll(): List<T> {
        val drainedValues = buildList { addAll(values) }
        values.clear()
        return drainedValues
    }

    /**
     * Clears all recorded values emitted by the [Flow].
     */
    fun reset() {
        values.clear()
    }
}
