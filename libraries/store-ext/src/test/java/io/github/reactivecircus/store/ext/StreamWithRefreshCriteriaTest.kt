package io.github.reactivecircus.store.ext

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.fresh
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.FlowRecorder
import io.github.reactivecircus.coroutines.test.ext.recordWith
import io.github.reactivecircus.store.ext.testutil.FetcherResponse
import io.github.reactivecircus.store.ext.testutil.FlowingTestPersister
import io.github.reactivecircus.store.ext.testutil.NonFlowingTestPersister
import io.github.reactivecircus.store.ext.testutil.TestFetcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@FlowPreview
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class StreamWithRefreshCriteriaTest {

    private val testScope = TestCoroutineScope()

    private val flowRecorder = FlowRecorder<StoreResponse<Int>>(testScope)

    private val inMemoryPersister = NonFlowingTestPersister<String, Int>()

    @Test
    fun `streamWithRefreshCriteria returns non-refreshing cached stream when refreshCriteria#shouldRefresh() returns false`() =
        testScope.runBlockingTest {
            val refreshCriteria = TestRefreshCriteria(shouldRefresh = false)
            val store = buildStoreWithNonFlowingPersister(
                fetcher = TestFetcher(FetcherResponse.Success(output = 1))
            )

            // given that data exists in persister
            store.fresh("key")

            store.streamWithRefreshCriteria("key", refreshCriteria)
                .recordWith(flowRecorder)

            assertThat(flowRecorder.takeAll())
                .containsExactly(
                    StoreResponse.Data(1, ResponseOrigin.Persister)
                )

            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(0)
        }

    @Test
    fun `streamWithRefreshCriteria returns refreshing cached stream when refreshCriteria#shouldRefresh() returns true`() =
        testScope.runBlockingTest {
            val refreshCriteria = TestRefreshCriteria(shouldRefresh = true)
            val store = buildStoreWithNonFlowingPersister(
                fetcher = TestFetcher(
                    FetcherResponse.Success(output = 1),
                    FetcherResponse.Success(output = 2)
                )
            )

            // given that data exists in persister
            store.fresh("key")

            store.streamWithRefreshCriteria("key", refreshCriteria)
                .recordWith(flowRecorder)

            assertThat(flowRecorder.takeAll())
                .containsExactly(
                    StoreResponse.Data(1, ResponseOrigin.Persister),
                    StoreResponse.Loading<Int>(ResponseOrigin.Fetcher),
                    StoreResponse.Data(2, ResponseOrigin.Fetcher)
                )

            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(1)
        }

    @Test
    fun `onRefreshed(key, output) is invoked when refreshCriteria#shouldRefresh() returns false and StoreResponse is Data and ResponseOrigin is Fetcher`() =
        testScope.runBlockingTest {
            val readerChannel = ConflatedBroadcastChannel(1)
            val refreshCriteria = TestRefreshCriteria(shouldRefresh = false)
            val store = buildStoreWithFlowingPersister(
                fetcher = TestFetcher(FetcherResponse.Success(output = 1)),
                persister = FlowingTestPersister(readerChannel.asFlow())
            )

            store.streamWithRefreshCriteria("key", refreshCriteria)
                .recordWith(flowRecorder)

            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(0)

            // a new response emitted from fetcher
            store.fresh("key")

            // onRefreshed(key, output) should have been invoked
            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(1)

            // another response emitted from persister
            readerChannel.offer(1)

            // onRefreshed(key, output) should not have been invoked again
            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(1)
        }

    @Test
    fun `onRefreshed(key, output) is invoked when refreshCriteria#shouldRefresh() returns true and StoreResponse is Data and ResponseOrigin is Fetcher`() =
        testScope.runBlockingTest {
            val readerChannel = ConflatedBroadcastChannel(1)
            val refreshCriteria = TestRefreshCriteria(shouldRefresh = true)
            val store = buildStoreWithFlowingPersister(
                fetcher = TestFetcher(
                    FetcherResponse.Success(output = 1),
                    FetcherResponse.Success(output = 2)
                ),
                persister = FlowingTestPersister(readerChannel.asFlow())
            )

            store.streamWithRefreshCriteria("key", refreshCriteria)
                .recordWith(flowRecorder)

            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(1)

            // a new response emitted from fetcher
            store.fresh("key")

            // onRefreshed(key, output) should have been invoked again
            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(2)

            // another response emitted from persister
            readerChannel.offer(1)

            // onRefreshed(key, output) should not have been invoked again
            assertThat(refreshCriteria.refreshCounts)
                .isEqualTo(2)
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun buildStoreWithNonFlowingPersister(
        fetcher: TestFetcher<String, Int>,
        persister: NonFlowingTestPersister<String, Int> = inMemoryPersister,
        scope: CoroutineScope = testScope
    ): Store<String, Int> {
        return StoreBuilder.fromNonFlow(fetcher::fetch)
            .nonFlowingPersister(
                reader = persister::read,
                writer = persister::write
            )
            .disableCache()
            .scope(scope)
            .build()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun buildStoreWithFlowingPersister(
        fetcher: TestFetcher<String, Int>,
        persister: FlowingTestPersister<String, Int>,
        scope: CoroutineScope = testScope
    ): Store<String, Int> {
        return StoreBuilder.fromNonFlow(fetcher::fetch)
            .persister(
                reader = persister::read,
                writer = persister::write
            )
            .disableCache()
            .scope(scope)
            .build()
    }
}

private class TestRefreshCriteria(
    private val shouldRefresh: Boolean
) : RefreshCriteria {

    private var _refreshCounts = 0

    val refreshCounts get() = _refreshCounts

    override suspend fun shouldRefresh(refreshScope: RefreshScope): Boolean = shouldRefresh

    override suspend fun onRefreshed(refreshScope: RefreshScope) {
        _refreshCounts++
    }
}
