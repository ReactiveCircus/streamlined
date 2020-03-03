package io.github.reactivecircus.streamlined.home

sealed class HomeState {
    abstract val items: List<FeedItem>?

    data class Idle(
        override val items: List<FeedItem>
    ) : HomeState()

    data class InFlight(
        override val items: List<FeedItem>? = null
    ) : HomeState()

    data class Error(
        override val items: List<FeedItem>? = null
    ) : HomeState()
}

sealed class HomeAction {
    object Refresh : HomeAction()
}

sealed class HomeEffect {
    object ShowTransientError : HomeEffect()
}
