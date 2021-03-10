package io.github.reactivecircus.streamlined.ui.storydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val storyId: Long,
    storyDetailsWorkflow: StoryDetailsWorkflow,
) : ViewModel() {
    @OptIn(WorkflowUiExperimentalApi::class)
    val rendering: Flow<StoryDetailsRendering> = renderWorkflowIn(
        workflow = storyDetailsWorkflow,
        scope = viewModelScope,
        props = MutableStateFlow(storyId),
    )

    @AssistedFactory
    interface Factory {
        fun create(storyId: Long): StoryDetailsViewModel
    }
}
