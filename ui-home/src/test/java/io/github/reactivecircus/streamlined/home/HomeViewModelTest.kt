package io.github.reactivecircus.streamlined.home

import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth.assertThat
import io.github.reactivecircus.coroutines.test.ext.CoroutinesTestRule
import io.github.reactivecircus.coroutines.test.ext.FlowRecorder
import io.github.reactivecircus.coroutines.test.ext.recordWith
import io.github.reactivecircus.streamlined.domain.interactor.FetchHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.FetchPersonalizedStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamPersonalizedStories
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.domain.repository.FakeResponse
import io.github.reactivecircus.streamlined.domain.repository.FakeStoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@FlowPreview
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {

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

    private val testScope = TestCoroutineScope()

    private val testDispatcher = TestCoroutineDispatcher()

    private val testDispatcherProvider = CoroutineDispatcherProvider(
        io = testDispatcher,
        computation = testDispatcher,
        ui = testDispatcher
    )

    private val fakeStoryRepository = FakeStoryRepository()

    private val streamHeadlineStories = StreamHeadlineStories(
        storyRepository = fakeStoryRepository,
        dispatcherProvider = testDispatcherProvider
    )

    private val streamPersonalizedStories = StreamPersonalizedStories(
        storyRepository = fakeStoryRepository,
        dispatcherProvider = testDispatcherProvider
    )

    private val fetchHeadlineStories = FetchHeadlineStories(
        storyRepository = fakeStoryRepository,
        dispatcherProvider = testDispatcherProvider
    )

    private val fetchPersonalizedStories = FetchPersonalizedStories(
        storyRepository = fakeStoryRepository,
        dispatcherProvider = testDispatcherProvider
    )

    private val stateRecorder = FlowRecorder<HomeState>(testScope)

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(
            HomeStateMachine(
                streamHeadlineStories,
                streamPersonalizedStories,
                fetchHeadlineStories,
                fetchPersonalizedStories,
                DefaultHomeUiConfigs(testDispatcherProvider)
            )
        )
    }

    @Test
    fun `starts streaming headline and personalized stories and emits InFlight#Initial state when initialized`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(emptyFlow())
            nextPersonalizedStoriesFlow(emptyFlow())
        }

        viewModel.state.recordWith(stateRecorder)

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial
            )
    }

    @Test
    fun `emits ShowingContent state with generated feed items when headline and personalized stories streams emit Data responses`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(
                flowOf(StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth))
            )
            nextPersonalizedStoriesFlow(
                flowOf(StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth))
            )
        }

        viewModel.state.recordWith(stateRecorder)

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.ShowingContent(expectedFeedItems(headlineStories, personalizedStories))
            )
    }

    @Test
    fun `emits Error#Permanent state when headline stories stream emits Data response and personalized stories stream emits Error response`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(
                flowOf(StoreResponse.Data(headlineStories, ResponseOrigin.Fetcher))
            )
            nextPersonalizedStoriesFlow(
                flowOf(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
            )
        }

        viewModel.state.recordWith(stateRecorder)

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.Error.Permanent
            )
    }

    @Test
    fun `emits Error#Permanent state when headline stories stream emits Error response and personalized stories stream emits Data response`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(
                flowOf(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
            )
            nextPersonalizedStoriesFlow(
                flowOf(StoreResponse.Data(personalizedStories, ResponseOrigin.Fetcher))
            )
        }

        viewModel.state.recordWith(stateRecorder)

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.Error.Permanent
            )
    }

    @Test
    fun `emits Error#Permanent state when both headline and personalized stories streams emit Error responses`() {
        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(
                flowOf(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
            )
            nextPersonalizedStoriesFlow(
                flowOf(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
            )
        }

        viewModel.state.recordWith(stateRecorder)

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.Error.Permanent
            )
    }

    @Test
    fun `emits InFlight#FetchWithCache state when either stories stream emits Loading response while in ShowingContent state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
        }

        viewModel.state.recordWith(stateRecorder)

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

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.ShowingContent(expectedFeedItems(headlineStories, personalizedStories)),
                HomeState.InFlight.FetchWithCache(
                    expectedFeedItems(
                        headlineStories,
                        personalizedStories
                    )
                )
            )
    }

    @Test
    fun `emits Error#Transient state when either stories stream emits Error response with existing cached data in the current state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
        }

        viewModel.state.recordWith(stateRecorder)

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

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.ShowingContent(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.FetchWithCache(
                    expectedFeedItems(
                        headlineStories,
                        personalizedStories
                    )
                ),
                HomeState.Error.Transient(
                    expectedFeedItems(headlineStories, personalizedStories)
                )
            )
    }

    @Test
    fun `emits ShowingContent state with updated feed items when refresh is successful while in ShowingContent state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
            nextFetchHeadlineStoriesResponse(FakeResponse.Success(headlineStories.subList(0, 1)))
            nextFetchPersonalizedStoriesResponse(FakeResponse.Success(emptyList()))
        }

        viewModel.state.recordWith(stateRecorder)

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

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.ShowingContent(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.Refresh(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.ShowingContent(
                    expectedFeedItems(headlineStories.subList(0, 1), personalizedStories)
                ),
                HomeState.ShowingContent(
                    expectedFeedItems(headlineStories.subList(0, 1), emptyList())
                )
            )
    }

    @Test
    fun `emits Error#Transient state with existing data when refresh is unsuccessful while in ShowingContent state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
            nextFetchHeadlineStoriesResponse(FakeResponse.Error(IOException()))
            nextFetchPersonalizedStoriesResponse(FakeResponse.Success(emptyList()))
        }

        viewModel.state.recordWith(stateRecorder)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth))
        }

        viewModel.refreshHomeFeeds()

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.ShowingContent(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.InFlight.Refresh(
                    expectedFeedItems(headlineStories, personalizedStories)
                ),
                HomeState.Error.Transient(
                    expectedFeedItems(headlineStories, personalizedStories)
                )
            )
    }

    @Test
    fun `emits ShowingContent state after being in the Error#Transient state for a certain period`() =
        testDispatcher.runBlockingTest {
            val headlineStoriesResponseEmitter =
                BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
            val personalizedStoriesResponseEmitter =
                BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

            fakeStoryRepository.scheduleNext {
                nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
                nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
                nextFetchHeadlineStoriesResponse(FakeResponse.Success(emptyList()))
                nextFetchPersonalizedStoriesResponse(FakeResponse.Error(IOException()))
            }

            viewModel.state.recordWith(stateRecorder)

            headlineStoriesResponseEmitter.run {
                offer(StoreResponse.Data(headlineStories, ResponseOrigin.SourceOfTruth))
            }
            personalizedStoriesResponseEmitter.run {
                offer(StoreResponse.Data(personalizedStories, ResponseOrigin.SourceOfTruth))
            }

            viewModel.refreshHomeFeeds()

            assertThat(stateRecorder.takeAll())
                .containsExactly(
                    HomeState.InFlight.Initial,
                    HomeState.ShowingContent(
                        expectedFeedItems(headlineStories, personalizedStories)
                    ),
                    HomeState.InFlight.Refresh(
                        expectedFeedItems(
                            headlineStories,
                            personalizedStories
                        )
                    ),
                    HomeState.Error.Transient(
                        expectedFeedItems(headlineStories, personalizedStories)
                    )
                )

            // advance virtual clock to when the transient error is expected to be dismissed
            advanceTimeBy(DefaultHomeUiConfigs.TRANSIENT_ERROR_DISPLAY_DURATION.toLongMilliseconds())

            assertThat(stateRecorder.takeAll())
                .containsExactly(
                    HomeState.ShowingContent(
                        expectedFeedItems(headlineStories, personalizedStories)
                    )
                )
        }

    @Test
    fun `emits ShowingContent state with fetched feed items when retry is successful while in Error state`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
            nextFetchHeadlineStoriesResponse(FakeResponse.Success(headlineStories))
            nextFetchPersonalizedStoriesResponse(FakeResponse.Success(personalizedStories))
        }

        viewModel.state.recordWith(stateRecorder)

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

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.Error.Permanent,
                HomeState.InFlight.Refresh(null),
                HomeState.ShowingContent(
                    expectedFeedItems(
                        headlineStories.subList(0, 1),
                        personalizedStories.subList(0, 1)
                    )
                )
            )
    }

    @Test
    fun `emits Error#Permanent state again after an unsuccessful retry when already in Error#Permanent state before the retry`() {
        val headlineStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)
        val personalizedStoriesResponseEmitter =
            BroadcastChannel<StoreResponse<List<Story>>>(Channel.BUFFERED)

        fakeStoryRepository.scheduleNext {
            nextHeadlineStoriesFlow(headlineStoriesResponseEmitter.asFlow())
            nextPersonalizedStoriesFlow(personalizedStoriesResponseEmitter.asFlow())
            nextFetchHeadlineStoriesResponse(FakeResponse.Success(emptyList()))
            nextFetchPersonalizedStoriesResponse(FakeResponse.Error(IOException()))
        }

        viewModel.state.recordWith(stateRecorder)

        headlineStoriesResponseEmitter.run {
            offer(StoreResponse.Error.Exception(IOException(), ResponseOrigin.Fetcher))
        }
        personalizedStoriesResponseEmitter.run {
            offer(StoreResponse.Data(personalizedStories, ResponseOrigin.Fetcher))
        }

        viewModel.refreshHomeFeeds()

        assertThat(stateRecorder.takeAll())
            .containsExactly(
                HomeState.InFlight.Initial,
                HomeState.Error.Permanent,
                HomeState.InFlight.Refresh(null),
                HomeState.Error.Permanent
            )
    }

    private fun expectedFeedItems(
        headlineStories: List<Story>,
        personalizedStories: List<Story>
    ) = generateFeedItems(
        DefaultHomeUiConfigs.NUMBER_OF_HEADLINES_DISPLAYED,
        headlineStories,
        personalizedStories
    )
}
