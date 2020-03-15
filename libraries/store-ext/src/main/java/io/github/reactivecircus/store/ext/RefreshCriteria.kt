package io.github.reactivecircus.store.ext

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse

/**
 * An API that defines the criteria for whether [Store.streamWithRefreshCriteria(key, refreshCriteria)]
 * should start by doing a fetch from network.
 */
interface RefreshCriteria<Key : Any, Output : Any> {

    /**
     * Returns whether the store should fetch fresh data from the network at the start of the stream.
     */
    suspend fun shouldRefresh(key: Key): Boolean

    /**
     * Called when a new [StoreResponse.Data] with [ResponseOrigin.Fetcher] is emitted from the stream.
     */
    suspend fun onRefreshed(key: Key, output: Output)
}
