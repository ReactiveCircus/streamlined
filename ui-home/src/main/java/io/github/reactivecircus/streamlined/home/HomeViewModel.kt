package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import io.github.reactivecircus.streamlined.domain.interactor.StreamHeadlineStories
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.ui.util.AdapterItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    streamHeadlineStories: StreamHeadlineStories
) : ViewModel() {

    val state: LiveData<HomeState> = flow {
        val items = mutableListOf<AdapterItem<Story, FeedType, Unit>>().apply {
            add(AdapterItem.Header(FeedType.TopHeadlines))
            add(AdapterItem.Footer(Unit))
            add(AdapterItem.Header(FeedType.ForYou))
        }

        emit(items)
    }
        .mapLatest { HomeState.Idle(it) }
        .catch { Timber.e(it, "flow cancelled") }
        .asLiveData()

    fun refreshHomeFeeds() {
        // TODO
    }
}

// TODO move to HomeStateMachine
sealed class HomeState {
    abstract val items: List<AdapterItem<Story, FeedType, Unit>>

    data class Idle(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
    data class InFlight(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
    data class Error(override val items: List<AdapterItem<Story, FeedType, Unit>>) : HomeState()
}
