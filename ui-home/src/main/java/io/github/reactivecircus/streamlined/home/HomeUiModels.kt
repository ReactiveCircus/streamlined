package io.github.reactivecircus.streamlined.home

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
