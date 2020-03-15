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
 * Returns a [Flow] of [StoreResponse] with the [StoreRequest.cached] request, using [refreshCriteria]
 * to decide whether to do a `refresh` at the start.
 */
@FlowPreview
@ExperimentalCoroutinesApi
fun <Key : Any, Output : Any> Store<Key, Output>.streamWithRefreshCriteria(
    key: Key,
    refreshCriteria: RefreshCriteria<Key, Output>
): Flow<StoreResponse<Output>> {
    return flow { emit(refreshCriteria.shouldRefresh(key)) }
        .flatMapConcat { shouldRefresh ->
            stream(StoreRequest.cached(key, refresh = shouldRefresh))
        }
        .onEach { response ->
            if (response is StoreResponse.Data && response.origin == ResponseOrigin.Fetcher) {
                refreshCriteria.onRefreshed(key, response.value)
            }
        }
}
