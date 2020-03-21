package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.store.ext.RefreshCriteria
import io.github.reactivecircus.store.ext.RefreshScope
import io.github.reactivecircus.streamlined.testing.TestRefreshCriteria.shouldRefresh

/**
 * Implementation of [RefreshCriteria] where [shouldRefresh] is always true.
 */
object TestRefreshCriteria : RefreshCriteria {

    override suspend fun shouldRefresh(refreshScope: RefreshScope): Boolean = true

    override suspend fun onRefreshed(refreshScope: RefreshScope) = Unit
}
