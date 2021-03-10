package io.github.reactivecircus.streamlined.ui.home

import com.dropbox.android.external.store4.StoreResponse
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.Worker
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import com.squareup.workflow1.asWorker
import com.squareup.workflow1.runningWorker
import com.squareup.workflow1.transform
import io.github.reactivecircus.streamlined.domain.interactor.FetchHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.FetchPersonalizedStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.interactor.StreamPersonalizedStories
import io.github.reactivecircus.streamlined.domain.model.Story
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import reactivecircus.blueprint.interactor.EmptyParams

class HomeWorkflow @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories,
    streamPersonalizedStories: StreamPersonalizedStories,
    private val fetchHeadlineStories: FetchHeadlineStories,
    private val fetchPersonalizedStories: FetchPersonalizedStories,
    private val homeUiConfigs: HomeUiConfigs
) : StatefulWorkflow<Unit, HomeState, Nothing, HomeRendering>() {

    // TODO replace with currently applied filters / configs
    private val query = "android"

    override fun initialState(props: Unit, snapshot: Snapshot?): HomeState {
        return HomeState.InFlight.Initial
    }

    override fun render(
        renderProps: Unit,
        renderState: HomeState,
        context: RenderContext,
    ): HomeRendering {
        context.runningWorker(streamCombinedStoriesWorker) {
            handleStreamStoriesResponse(it, homeUiConfigs)
        }

        when (renderState) {
            is HomeState.InFlight.Refresh -> {
                context.runningWorker(refreshStoriesWorker) {
                    handleRefreshStoriesResponse(it)
                }
            }
            is HomeState.Error.Transient -> {
                context.runningWorker(
                    Worker.timer(homeUiConfigs.transientErrorDisplayDuration.inMilliseconds.toLong())
                        .transform { it.flowOn(homeUiConfigs.delayDispatcher) }
                ) {
                    onDismissTransientError()
                }
            }
            else -> Unit
        }

        return HomeRendering(renderState, onRefresh = context.eventHandler {
            this.state = HomeState.InFlight.Refresh(renderState.itemsOrNull)
        })
    }

    override fun snapshotState(state: HomeState): Snapshot? = null

    private fun handleStreamStoriesResponse(
        combinedResponses: CombinedStoryResponses,
        homeUiConfigs: HomeUiConfigs
    ): HomeAction = action {
        val currentState = state
        val headlines = combinedResponses.first
        val personalized = combinedResponses.second
        state = when {
            // When either response is loading and currently showing content,
            // transition to InFlight.FetchWithCache state.
            // This is needed when cached content has been displayed,
            // but the story streams continue to fetch fresh data from network after
            // emitting the cached content.
            // The sequence of responses looks like this:
            // Data response cache / disk -> Loading -> Error or Data response from network
            headlines is StoreResponse.Loading || personalized is StoreResponse.Loading -> {
                when (currentState) {
                    is HomeState.ShowingContent -> {
                        HomeState.InFlight.FetchWithCache(currentState.items)
                    }
                    else -> currentState
                }
            }
            // When either response is an error, transition to Error.Permanent state
            // if we are either loading for the first time;
            // or transition to Error.Transient state if we are fetching from network
            // while displaying cached content.
            headlines is StoreResponse.Error || personalized is StoreResponse.Error -> {
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
            // Generate feed items and transition to ShowingContent state
            // when both responses have data available
            headlines is StoreResponse.Data && personalized is StoreResponse.Data -> {
                val feedItems = generateFeedItems(
                    maxNumberOfHeadlines = homeUiConfigs.numberOfHeadlinesDisplayed,
                    headlineStories = headlines.requireData(),
                    personalizedStories = personalized.requireData()
                )
                HomeState.ShowingContent(feedItems)
            }
            else -> currentState
        }
    }

    private fun handleRefreshStoriesResponse(successful: Boolean): HomeAction = action {
        // When refresh fails, transition to Error.Permanent state if no existing data is available,
        // otherwise transition to Error.Transient state if data exists.
        val currentState = state
        state = if (!successful && currentState is HomeState.InFlight.Refresh) {
            if (currentState.items == null) {
                HomeState.Error.Permanent
            } else {
                HomeState.Error.Transient(currentState.items)
            }
        } else {
            currentState
        }
    }

    private fun onDismissTransientError(): HomeAction = action {
        val currentState = state
        state = when (currentState) {
            is HomeState.Error.Transient -> {
                HomeState.ShowingContent(currentState.items)
            }
            else -> currentState
        }
    }

    /**
     * A [Worker] that combines headline and personalized story streams.
     */
    private val streamCombinedStoriesWorker: Worker<CombinedStoryResponses> = combine(
        streamHeadlineStories.buildFlow(EmptyParams),
        streamPersonalizedStories.buildFlow(StreamPersonalizedStories.Params(query))
    ) { source1, source2 -> source1 to source2 }.asWorker()

    /**
     * A [Worker] that fetch both headlines and personalized stories from network.
     * We only care if the fetching fails and don't care about the result which will be emitted
     * through the combined stories stream.
     */
    private val refreshStoriesWorker: Worker<Boolean> = Worker.from {
        @Suppress("TooGenericExceptionCaught")
        try {
            coroutineScope {
                val headlineStoriesDeferred = async {
                    fetchHeadlineStories.execute(EmptyParams)
                }
                val personalizedStoriesDeferred = async {
                    fetchPersonalizedStories.execute(
                        FetchPersonalizedStories.Params(query)
                    )
                }
                awaitAll(headlineStoriesDeferred, personalizedStoriesDeferred)
            }
            true
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            false
        }
    }
}

private typealias HomeAction = WorkflowAction<Unit, HomeState, Nothing>

private typealias CombinedStoryResponses = Pair<StoreResponse<List<Story>>, StoreResponse<List<Story>>>
