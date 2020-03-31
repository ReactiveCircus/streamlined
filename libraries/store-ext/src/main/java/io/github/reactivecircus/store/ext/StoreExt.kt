package io.github.reactivecircus.store.ext

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

/**
 * Returns a [Flow] of [StoreResponse] with the [StoreRequest.cached] request, using [refreshPolicy]
 * to decide whether to do a `refresh` at the start.
 */
@FlowPreview
@ExperimentalCoroutinesApi
inline fun <reified Key : Any, reified Output : Any> Store<Key, Output>.streamWithRefreshPolicy(
    key: Key,
    refreshPolicy: RefreshPolicy
): Flow<StoreResponse<Output>> {
    val refreshScope = getRefreshScope<Key, Output>(key)
    return flow { emit(refreshPolicy.shouldRefresh(refreshScope)) }
        .flatMapConcat { shouldRefresh ->
            stream(StoreRequest.cached(key, refresh = shouldRefresh))
        }
        .onEach { response ->
            if (response is StoreResponse.Data && response.origin == ResponseOrigin.Fetcher) {
                refreshPolicy.onRefreshed(refreshScope)
            }
        }
}

/**
 * Generates the refreshScope of the [RefreshPolicy] from a [Store]'s Output type and a [key].
 */
@PublishedApi
internal inline fun <reified Key : Any, reified Output : Any> getRefreshScope(
    key: Key
): RefreshScope {
    return RefreshScope("$key ${Output::class.java}")
}
