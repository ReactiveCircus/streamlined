package io.github.reactivecircus.streamlined.home

import io.github.reactivecircus.streamlined.domain.model.Story

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

sealed class HomeState {
    data class Idle(val items: List<FeedItem>) : HomeState()

    sealed class InFlight : HomeState() {
        abstract val items: List<FeedItem>?

        data class FirstTime(override val items: List<FeedItem>?) : InFlight()
        data class Subsequent(override val items: List<FeedItem>?) : InFlight()
    }

    object Error : HomeState()

    internal val itemsOrNull: List<FeedItem>?
        get() = when (this) {
            is Idle -> items
            is InFlight -> items
            else -> null
        }
}

sealed class HomeAction {
    object Refresh : HomeAction()
}

sealed class HomeEffect {
    object ShowTransientError : HomeEffect()
}
