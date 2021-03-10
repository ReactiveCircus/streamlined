package io.github.reactivecircus.streamlined.ui.testing

import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.store.ext.RefreshScope
import io.github.reactivecircus.streamlined.ui.testing.TestRefreshPolicy.shouldRefresh

/**
 * Implementation of [RefreshPolicy] where [shouldRefresh] is always true.
 */
object TestRefreshPolicy : RefreshPolicy {

    override suspend fun shouldRefresh(refreshScope: RefreshScope): Boolean = true

    override suspend fun onRefreshed(refreshScope: RefreshScope) = Unit
}
