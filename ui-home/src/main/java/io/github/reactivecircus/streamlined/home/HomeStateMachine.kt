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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import reactivecircus.blueprint.interactor.EmptyParams
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class HomeStateMachine @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories,
    streamPersonalizedStories: StreamPersonalizedStories,
    private val fetchHeadlineStories: FetchHeadlineStories,
    private val fetchPersonalizedStories: FetchPersonalizedStories,
    private val homeUiConfigs: HomeUiConfigs
) : FlowReduxStateMachine<HomeState, HomeAction>(HomeState.InFlight.Initial) {

    // TODO replace with currently applied filters / configs
    private val query = "android"

    init {
        spec {
            inState<HomeState.InFlight.Refresh> {
                onEnter(FlatMapPolicy.LATEST, ::fetchStoriesFromNetwork)
            }

            inState<HomeState.ShowingContent> {
                on<HomeAction.Refresh>(FlatMapPolicy.LATEST) { _, _, setState ->
                    setState { HomeState.InFlight.Refresh(it.itemsOrNull) }
                }
            }

            inState<HomeState.Error> {
                on<HomeAction.Refresh>(FlatMapPolicy.LATEST) { _, _, setState ->
                    setState { HomeState.InFlight.Refresh(it.itemsOrNull) }
                }
            }

            inState<HomeState.Error.Transient> {
                val timer = flow {
                    delay(homeUiConfigs.transientErrorDisplayDuration)
                    emit(Unit)
                }.flowOn(homeUiConfigs.delayDispatcher)
                collectWhileInState(timer, FlatMapPolicy.LATEST, ::dismissTransientError)
            }

            val combinedStories = combine(
                streamHeadlineStories.buildFlow(EmptyParams),
                streamPersonalizedStories.buildFlow(StreamPersonalizedStories.Params(query))
            ) { source1, source2 -> source1 to source2 }

            collectWhileInAnyState(
                combinedStories, FlatMapPolicy.LATEST, ::processCombinedStoriesFlow
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
        getState: GetState<HomeState>,
        setState: SetState<HomeState>
    ) {
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

        // When refresh fails, transition to Error.Permanent state if no existing data is available,
        // otherwise transition to Error.Transient state if data exists.
        setState { currentState ->
            if (result.isFailure && currentState is HomeState.InFlight.Refresh) {
                if (currentState.items == null) {
                    HomeState.Error.Permanent
                } else {
                    HomeState.Error.Transient(currentState.items)
                }
            } else {
                currentState
            }
        }
    }

    /**
     * Set the next state based on the latest [StoreResponse] type
     * for both the headlines and personalized stories.
     */
    @Suppress("UNUSED_PARAMETER")
    private suspend fun processCombinedStoriesFlow(
        combinedStoryResponses: CombinedStoryResponses,
        getState: GetState<HomeState>,
        setState: SetState<HomeState>
    ) {
        val headlines = combinedStoryResponses.first
        val personalized = combinedStoryResponses.second

        when {
            // When either response is loading and currently showing content,
            // transition to InFlight.FetchWithCache state.
            // This is needed when cached content has been displayed,
            // but the story streams continue to fetch fresh data from network after
            // emitting the cached content.
            // The sequence of responses looks like this:
            // Data response cache / disk -> Loading -> Error or Data response from network
            headlines is StoreResponse.Loading || personalized is StoreResponse.Loading -> {
                setState { currentState ->
                    when (currentState) {
                        is HomeState.ShowingContent -> {
                            HomeState.InFlight.FetchWithCache(currentState.items)
                        }
                        else -> currentState
                    }
                }
            }
            // When either response is an error, transition to Error.Permanent state
            // if we are either loading for the first time;
            // or transition to Error.Transient state if we are fetching from network
            // while displaying cached content.
            headlines is StoreResponse.Error || personalized is StoreResponse.Error -> {
                setState { currentState ->
                    when (currentState) {
                        is HomeState.InFlight.Initial -> {
                            HomeState.Error.Permanent
                        }
                        is HomeState.InFlight.FetchWithCache -> {
                            HomeState.Error.Transient(currentState.items)
                        }
                        else -> currentState
                    }
                }
            }
            // Generate feed items and transition to ShowingContent state
            // when both responses have data available
            headlines is StoreResponse.Data && personalized is StoreResponse.Data -> {
                val feedItems = generateFeedItems(
                    maxNumberOfHeadlines = homeUiConfigs.numberOfHeadlinesDisplayed,
                    headlineStories = headlines.requireData(),
                    personalizedStories = personalized.requireData()
                )
                setState {
                    HomeState.ShowingContent(feedItems)
                }
            }
        }
    }

    /**
     * Reset state back to ShowingContent after displaying transient error for a period.
     */
    @Suppress("UNUSED_PARAMETER")
    private suspend fun dismissTransientError(
        result: Unit,
        getState: GetState<HomeState>,
        setState: SetState<HomeState>
    ) {
        setState { currentState ->
            when (currentState) {
                is HomeState.Error.Transient -> {
                    HomeState.ShowingContent(currentState.items)
                }
                else -> currentState
            }
        }
    }
}

internal typealias CombinedStoryResponses = Pair<StoreResponse<List<Story>>, StoreResponse<List<Story>>>
