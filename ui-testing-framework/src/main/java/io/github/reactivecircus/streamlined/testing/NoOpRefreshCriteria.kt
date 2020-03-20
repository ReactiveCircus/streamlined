package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.store.ext.RefreshCriteria
import io.github.reactivecircus.store.ext.RefreshScope
import io.github.reactivecircus.streamlined.testing.NoOpRefreshCriteria.shouldRefresh

/**
 * Implementation of [RefreshCriteria] where [shouldRefresh] is always false.
 */
object NoOpRefreshCriteria : RefreshCriteria {

    override suspend fun shouldRefresh(refreshScope: RefreshScope): Boolean = false

    override suspend fun onRefreshed(refreshScope: RefreshScope) = Unit
}
