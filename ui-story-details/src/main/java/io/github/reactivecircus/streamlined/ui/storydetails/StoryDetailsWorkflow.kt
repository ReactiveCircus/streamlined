package io.github.reactivecircus.streamlined.ui.storydetails

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.parse
import io.github.reactivecircus.streamlined.domain.interactor.GetStoryById
import javax.inject.Inject

class StoryDetailsWorkflow @Inject constructor(
    private val getStoryById: GetStoryById
) : StatefulWorkflow<Long, StoryDetailsState, Nothing, StoryDetailsRendering>() {

    override fun initialState(props: Long, snapshot: Snapshot?): StoryDetailsState {
        return StoryDetailsState.InFlight(
            storyId = snapshot?.bytes?.parse { source -> source.readLong() } ?: props
        )
    }

    override fun render(
        renderProps: Long,
        renderState: StoryDetailsState,
        context: RenderContext,
    ): StoryDetailsRendering {
        getStoryById

        return StoryDetailsRendering(
            renderState,
            onAddToReadingList = {},
            onRemoveFromReadingList = {}
        )
    }

    override fun snapshotState(state: StoryDetailsState): Snapshot = Snapshot.write { sink ->
        sink.writeLong(state.storyId)
    }
}

private typealias StoryDetailsAction = WorkflowAction<Long, StoryDetailsState, Nothing>
