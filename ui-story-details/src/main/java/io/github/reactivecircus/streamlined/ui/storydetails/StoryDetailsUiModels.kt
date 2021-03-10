package io.github.reactivecircus.streamlined.ui.storydetails

data class StoryDetailsRendering(
    val state: StoryDetailsState,
    val onAddToReadingList: () -> Unit,
    val onRemoveFromReadingList: () -> Unit
)

sealed class StoryDetailsState {
    abstract val storyId: Long

    data class InFlight(override val storyId: Long) : StoryDetailsState()
}
