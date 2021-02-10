@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.testing.assumption

import dagger.Reusable
import javax.inject.Inject
import retrofit2.mock.NetworkBehavior

@Reusable
class NetworkAssumptions @Inject constructor(
    private val networkBehavior: NetworkBehavior
) {
    fun assumeNetworkConnected() {
        networkBehavior.setFailurePercent(0)
    }

    fun assumeNetworkDisconnected() {
        networkBehavior.setFailurePercent(100)
    }
}
