package io.github.reactivecircus.coroutines.test.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.recordWith(recorder: FlowRecorder<T>) {
    onEach { recorder.values.add(it) }.launchIn(recorder.coroutineScope)
}

class FlowRecorder<T>(internal val coroutineScope: CoroutineScope) {

    internal val values = mutableListOf<T>()

    val recordedValues: List<T> get() = values

    fun reset() {
        values.clear()
    }
}
