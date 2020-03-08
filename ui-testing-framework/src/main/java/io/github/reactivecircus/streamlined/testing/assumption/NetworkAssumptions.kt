@file:Suppress("MagicNumber")

package io.github.reactivecircus.streamlined.testing.assumption

import androidx.test.core.app.ApplicationProvider
import io.github.reactivecircus.streamlined.testing.di.TestingFrameworkComponent
import retrofit2.mock.NetworkBehavior
import javax.inject.Inject

class NetworkAssumptions @Inject constructor(
    private val networkBehavior: NetworkBehavior
) {
    internal fun assumeNetworkConnected() {
        networkBehavior.setFailurePercent(0)
    }

    internal fun assumeNetworkDisconnected() {
        networkBehavior.setFailurePercent(100)
    }
}

private val networkAssumptions = TestingFrameworkComponent
    .getOrCreate(ApplicationProvider.getApplicationContext())
    .networkAssumptions

fun assumeNetworkConnected() {
    networkAssumptions.assumeNetworkConnected()
}

fun assumeNetworkDisconnected() {
    networkAssumptions.assumeNetworkDisconnected()
}
