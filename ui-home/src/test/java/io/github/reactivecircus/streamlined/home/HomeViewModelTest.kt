package io.github.reactivecircus.streamlined.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.CoroutinesTestRule
import io.github.reactivecircus.coroutines.test.ext.FlowRecorder
import io.github.reactivecircus.coroutines.test.ext.recordWith
import io.github.reactivecircus.livedata.test.ext.TestObserver
import io.github.reactivecircus.streamlined.domain.interactor.FetchHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.FetchPersonalizedStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamPersonalizedStories
import io.github.reactivecircus.streamlined.domain.model.Story
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@FlowPreview
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val headlineStories = listOf(
        Story(
            id = 1,
            source = "source1",
            title = "Article 1",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 1000L
        ),
        Story(
            id = 2,
            source = "source2",
            title = "Article 2",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 2000L
        )
    )

    private val personalizedStories = listOf(
        Story(
            id = 3,
            source = "source3",
            title = "Article 3",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 3000L
        ),
        Story(
            id = 4,
            source = "source4",
            title = "Article 4",
            author = "Yang",
            description = "Description...",
            url = "url",
            imageUrl = "image-url",
            publishedTime = 4000L
        )
    )

    private val streamHeadlineStories = mockk<StreamHeadlineStories>()

    private val streamPersonalizedStories = mockk<StreamPersonalizedStories>()

    private val fetchHeadlineStories = mockk<FetchHeadlineStories>()

    private val fetchPersonalizedStories = mockk<FetchPersonalizedStories>()

    private val stateObserver = TestObserver<HomeState>()

    private val effectFlowRecorder = FlowRecorder<HomeEffect>(TestCoroutineScope())

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(
            HomeStateMachine(
                streamHeadlineStories,
                streamPersonalizedStories,
                fetchHeadlineStories,
                fetchPersonalizedStories
            )
        )
    }

    @Test
    fun `starts streaming headline and personalized stories and emits InFlight#FirstTime state when initialized`() {
        every { streamHeadlineStories.buildFlow(any()) } returns emptyFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns emptyFlow()

        viewModel.state.observeForever(stateObserver)

        verifyAll {
            streamHeadlineStories.buildFlow(any())
            streamPersonalizedStories.buildFlow(any())
        }

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null)
            )
    }

    @Test
    fun `emits Idle state with generated feed items when headline and personalized stories streams emit Data responses`() {
        every { streamHeadlineStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth)
        )
        every { streamPersonalizedStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth)
        )

        viewModel.state.observeForever(stateObserver)

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.Idle(expectedFeedItems(headlineStories, personalizedStories))
            )
    }

    @Test
    fun `emits Error state when headline stories stream emits Data response and personalized stories stream emits Error response`() {
        every { streamHeadlineStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Data(headlineStories, ResponseOrigin.Fetcher)
        )
        every { streamPersonalizedStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher)
        )

        viewModel.state.observeForever(stateObserver)

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.Error
            )
    }

    @Test
    fun `emits Error state when headline stories stream emits Error response and personalized stories stream emits Data response`() {
        every { streamHeadlineStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher)
        )
        every { streamPersonalizedStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Data(personalizedStories, ResponseOrigin.Fetcher)
        )

        viewModel.state.observeForever(stateObserver)

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.Error
            )
    }

    @Test
    fun `emits InFlight#FirstTime state when either stories stream emits Loading response while not already in InFlight state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        viewModel.state.observeForever(stateObserver)

        // cached data is emitted first
        headlineStoriesResponseEmitter.offer(
            StoreResponse.Data(
                headlineStories,
                ResponseOrigin.SourceOfTruth
            )
        )
        personalizedStoriesResponseEmitter.offer(
            StoreResponse.Data(
                personalizedStories,
                ResponseOrigin.SourceOfTruth
            )
        )

        // then loading responses are emitted while fetching data from network
        headlineStoriesResponseEmitter.offer(
            StoreResponse.Loading(ResponseOrigin.Fetcher)
        )
        personalizedStoriesResponseEmitter.offer(
            StoreResponse.Loading(ResponseOrigin.Fetcher)
        )

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Idle(expectedFeedItems(headlineStories, personalizedStories)),
                HomeState.InFlight.FirstTime(
                    expectedFeedItems(
                        headlineStories,
                        personalizedStories
                    )
                )
            )
    }

    @Test
    fun `emits Error state when both headline and personalized stories streams emit Error responses`() {
        every { streamHeadlineStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher)
        )
        every { streamPersonalizedStories.buildFlow(any()) } returns flowOf(
            StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher)
        )

        viewModel.state.observeForever(stateObserver)

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.Error
            )
    }

    @Test
    fun `emits ShowTransientError effect without emitting new state when either stories stream emits Error response with existing cached data in the current state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        viewModel.state.observeForever(stateObserver)
        viewModel.effect.recordWith(effectFlowRecorder)

        // cached data is emitted first
        headlineStoriesResponseEmitter.offer(
            StoreResponse.Data(
                headlineStories,
                ResponseOrigin.SourceOfTruth
            )
        )
        personalizedStoriesResponseEmitter.offer(
            StoreResponse.Data(
                personalizedStories,
                ResponseOrigin.SourceOfTruth
            )
        )

        // then loading responses are emitted while fetching data from network
        headlineStoriesResponseEmitter.offer(
            StoreResponse.Loading(ResponseOrigin.Fetcher)
        )
        personalizedStoriesResponseEmitter.offer(
            StoreResponse.Loading(ResponseOrigin.Fetcher)
        )

        // then error returned when fetching data from network
        headlineStoriesResponseEmitter.offer(
            StoreResponse.Data(
                headlineStories,
                ResponseOrigin.Fetcher
            )
        )
        personalizedStoriesResponseEmitter.offer(
            StoreResponse.Error.Exception(
                IOException(),
                ResponseOrigin.Fetcher
            )
        )

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.FirstTime(
                    expectedFeedItems(
                        headlineStories,
                        personalizedStories
                    )
                ),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, personalizedStories)
                )
            )

        assertThat(effectFlowRecorder.takeAll())
            .containsExactly(HomeEffect.ShowTransientError)
    }

    @Test
    fun `emits Idle state with updated feed items when refresh is successful while in Idle state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        coEvery { fetchHeadlineStories.execute(any()) } returns headlineStories.subList(0, 1)
        coEvery { fetchPersonalizedStories.execute(any()) } returns emptyList()

        viewModel.state.observeForever(stateObserver)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth))
        }

        viewModel.refreshHomeFeeds()

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories.subList(0, 1), ResponseOrigin.Fetcher))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(emptyList(), ResponseOrigin.Fetcher))
        }

        coVerifyAll {
            fetchHeadlineStories.execute(any())
            fetchPersonalizedStories.execute(any())
        }

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.Subsequent(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.Idle(
                    expectedFeedItems(headlineStories.subList(0, 1), personalizedStories)
                ),
                HomeState.Idle(
                    expectedFeedItems(headlineStories.subList(0, 1), emptyList())
                )
            )
    }

    @Test
    fun `emits ShowTransientError effect and Idle state with existing data when refresh is unsuccessful while in Idle state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        coEvery { fetchHeadlineStories.execute(any()) } coAnswers { throw IOException() }
        coEvery { fetchPersonalizedStories.execute(any()) } returns emptyList()

        viewModel.state.observeForever(stateObserver)
        viewModel.effect.recordWith(effectFlowRecorder)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth))
        }

        viewModel.refreshHomeFeeds()

        // only refresh for personalized stories was successfully
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(emptyList(), ResponseOrigin.Fetcher))
        }

        coVerifyAll {
            fetchHeadlineStories.execute(any())
            fetchHeadlineStories.execute(any())
        }

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.Subsequent(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.Idle(
                    expectedFeedItems(headlineStories, emptyList())
                )
            )

        assertThat(effectFlowRecorder.takeAll())
            .containsExactly(HomeEffect.ShowTransientError)
    }

    @Test
    fun `emits Idle state with fetched feed items when retry is successful while in Error state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        coEvery { fetchHeadlineStories.execute(any()) } returns headlineStories
        coEvery { fetchPersonalizedStories.execute(any()) } returns personalizedStories

        viewModel.state.observeForever(stateObserver)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories, ResponseOrigin.Fetcher))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
        }

        viewModel.refreshHomeFeeds()

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories.subList(0, 1), ResponseOrigin.Fetcher))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories.subList(0, 1), ResponseOrigin.Fetcher))
        }

        coVerifyAll {
            fetchHeadlineStories.execute(any())
            fetchPersonalizedStories.execute(any())
        }

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Error,
                HomeState.InFlight.Subsequent(null),
                HomeState.Idle(
                    expectedFeedItems(
                        headlineStories.subList(0, 1),
                        personalizedStories.subList(0, 1)
                    )
                )
            )
    }

    @Test
    fun `emits Error state without emitting ShowTransientError effect when retry is unsuccessful while in Error state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        every { streamHeadlineStories.buildFlow(any()) } returns headlineStoriesResponseEmitter.asFlow()
        every { streamPersonalizedStories.buildFlow(any()) } returns personalizedStoriesResponseEmitter.asFlow()

        coEvery { fetchHeadlineStories.execute(any()) } returns emptyList()
        coEvery { fetchPersonalizedStories.execute(any()) } coAnswers { throw IOException() }

        viewModel.state.observeForever(stateObserver)
        viewModel.effect.recordWith(effectFlowRecorder)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories, ResponseOrigin.Fetcher))
        }

        viewModel.refreshHomeFeeds()

        coVerifyAll {
            fetchHeadlineStories.execute(any())
            fetchPersonalizedStories.execute(any())
        }

        assertThat(stateObserver.takeAll())
            .containsExactly(
                HomeState.InFlight.FirstTime(null),
                HomeState.Error,
                HomeState.InFlight.Subsequent(null),
                HomeState.Error
            )

        assertThat(effectFlowRecorder.takeAll())
            .isEmpty()
    }
}

private fun expectedFeedItems(
    headlineStories: List<Story>,
    personalizedStories: List<Story>
) = generateFeedItems(
    NUMBER_OF_HEADLINES_DISPLAYED,
    headlineStories,
    personalizedStories
)
