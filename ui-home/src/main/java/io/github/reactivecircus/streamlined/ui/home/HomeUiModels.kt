package io.github.reactivecircus.streamlined.ui.home

import io.github.reactivecircus.streamlined.domain.model.Story

data class HomeRendering(
    val state: HomeState,
    val onRefresh: () -> Unit
)

sealed class HomeState {
    sealed class InFlight : HomeState() {
        object Initial : InFlight()
        data class FetchWithCache(val items: List<FeedItem>) : InFlight()
        data class Refresh(val items: List<FeedItem>?) : InFlight()
    }

    data class ShowingContent(val items: List<FeedItem>) : HomeState()

    sealed class Error : HomeState() {
        data class Transient(val items: List<FeedItem>) : Error()
        object Permanent : Error()
    }

    internal val itemsOrNull: List<FeedItem>?
        get() = when (this) {
            is InFlight.FetchWithCache -> items
            is InFlight.Refresh -> items
            is ShowingContent -> items
            is Error.Transient -> items
            else -> null
        }
}

sealed class FeedItem {
    data class Header(val feedType: FeedType) : FeedItem()
    data class Content(val feedType: FeedType, val story: Story) : FeedItem()
    object TopHeadlinesFooter : FeedItem()
    data class Empty(val feedType: FeedType) : FeedItem()
}

sealed class FeedType {
    object TopHeadlines : FeedType()
    object ForYou : FeedType()
}
