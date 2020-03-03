package io.github.reactivecircus.streamlined.home

import com.dropbox.android.external.store4.StoreResponse
import com.freeletics.flowredux.GetState
import com.freeletics.flowredux.dsl.FlatMapPolicy
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.freeletics.flowredux.dsl.SetState
import io.github.reactivecircus.streamlined.domain.interactor.FetchHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.FetchPersonalizedStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamPersonalizedStories
import io.github.reactivecircus.streamlined.domain.model.Story
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.zip
import reactivecircus.blueprint.interactor.EmptyParams
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class HomeStateMachine @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories,
    streamPersonalizedStories: StreamPersonalizedStories,
    private val fetchHeadlineStories: FetchHeadlineStories,
    private val fetchPersonalizedStories: FetchPersonalizedStories
) : FlowReduxStateMachine<HomeState, HomeAction>(HomeState.InFlight()) {

    // TODO replace with currently applied filters / configs
    private val query = "android"

    private val effectEmitter = BroadcastChannel<HomeEffect>(Channel.BUFFERED)

    val effect: Flow<HomeEffect> = effectEmitter.asFlow()

    init {
        spec {
            inState<HomeState.Idle> {
                on<HomeAction.Refresh>(FlatMapPolicy.LATEST, ::fetchStoriesFromNetwork)
            }

            inState<HomeState.Error> {
                on<HomeAction.Refresh>(FlatMapPolicy.LATEST, ::fetchStoriesFromNetwork)
            }

            // TODO set refresh dynamically
            val headlineStoriesFlow = streamHeadlineStories
                .buildFlow(StreamHeadlineStories.Params(refresh = true))

            // TODO set refresh dynamically
            val personalizedStoriesFlow = streamPersonalizedStories
                .buildFlow(StreamPersonalizedStories.Params(query, refresh = true))

            val combinedStories = headlineStoriesFlow
                .zip(personalizedStoriesFlow) { source1, source2 -> source1 to source2 }

            collectWhileInAnyState(
                combinedStories,
                FlatMapPolicy.LATEST,
                ::processCombinedStoriesFlow
            )
        }
    }

    /**
     * Fetch both headlines and personalized stories from network.
     * We only care if the fetching fails and don't care about the result which will be emitted
     * through the combined stories stream which the state machine observes in all states.
     */
    @Suppress("UNUSED_PARAMETER")
    private suspend fun fetchStoriesFromNetwork(
        action: HomeAction,
        getState: GetState<HomeState>,
        setState: SetState<HomeState>
    ) {
        setState {
            HomeState.InFlight(getState().items)
        }

        val result = runCatching {
            coroutineScope {
                val headlineStoriesDeferred = async {
                    fetchHeadlineStories.execute(EmptyParams)
                }
                val personalizedStoriesDeferred = async {
                    fetchPersonalizedStories.execute(FetchPersonalizedStories.Params(query))
                }
                awaitAll(headlineStoriesDeferred, personalizedStoriesDeferred)
            }
        }

        // when refresh fails, transition to Error state if no existing data is available,
        // otherwise transition back to Idle state and emit an Effect for showing transient error
        val currentItems = getState().items
        setState(runIf = { result.isFailure }) {
            if (currentItems == null) {
                HomeState.Error(null)
            } else {
                HomeState.Idle(currentItems)
            }
        }
        if (result.isFailure && currentItems != null) {
            effectEmitter.offer(HomeEffect.ShowTransientError)
        }
    }

    /**
     * Set the next state based on the latest [StoreResponse] type
     * for both the headlines and personalized stories.
     */
    private suspend fun processCombinedStoriesFlow(
        combinedStoryResponses: CombinedStoryResponses,
        getState: GetState<HomeState>,
        setState: SetState<HomeState>
    ) {
        val headlines = combinedStoryResponses.first
        val personalized = combinedStoryResponses.second

        when {
            headlines is StoreResponse.Error || personalized is StoreResponse.Error -> {
                // when either response is error, transition to Error state if no existing data is available,
                // otherwise transition back to Idle state and emit an Effect for showing transient error
                val currentItems = getState().items
                setState {
                    if (currentItems == null) {
                        HomeState.Error(null)
                    } else {
                        HomeState.Idle(currentItems)
                    }
                }
                if (currentItems != null) {
                    effectEmitter.offer(HomeEffect.ShowTransientError)
                }
            }
            headlines is StoreResponse.Data || personalized is StoreResponse.Data -> {
                val feedItems = generateFeedItems(
                    maxNumberOfHeadlines = NUMBER_OF_HEADLINES_DISPLAYED,
                    headlineStories = headlines.requireData(),
                    personalizedStories = personalized.requireData()
                )
                setState {
                    HomeState.Idle(feedItems)
                }
            }
        }
    }
}

private typealias CombinedStoryResponses = Pair<StoreResponse<List<Story>>, StoreResponse<List<Story>>>
