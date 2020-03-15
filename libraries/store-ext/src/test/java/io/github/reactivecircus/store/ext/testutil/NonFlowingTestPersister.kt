package io.github.reactivecircus.store.ext.testutil

class NonFlowingTestPersister<Key : Any, Output : Any> {

    private val data = mutableMapOf<Key, Output>()

    @Suppress("RedundantSuspendModifier")
    suspend fun read(key: Key): Output? = data[key]

    @Suppress("RedundantSuspendModifier")
    suspend fun write(key: Key, output: Output) {
        data[key] = output
    }
}
