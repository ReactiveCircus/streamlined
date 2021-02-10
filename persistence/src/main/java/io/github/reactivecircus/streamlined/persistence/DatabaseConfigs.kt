package io.github.reactivecircus.streamlined.persistence

import kotlin.coroutines.CoroutineContext

class DatabaseConfigs(
    val databaseName: String?,
    val coroutineContext: CoroutineContext,
)
