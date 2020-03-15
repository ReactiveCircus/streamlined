package io.github.reactivecircus.store.ext.testutil

import kotlinx.coroutines.flow.Flow

class FlowingTestPersister<Key : Any, Output : Any>(private val responseFlow: Flow<Output?>) {

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    fun read(key: Key): Flow<Output?> = responseFlow

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    suspend fun write(key: Key, output: Output) = Unit
}
