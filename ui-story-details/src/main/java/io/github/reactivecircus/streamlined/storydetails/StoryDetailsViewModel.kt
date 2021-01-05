package io.github.reactivecircus.streamlined.storydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val storyId: Long,
    storyDetailsWorkflow: StoryDetailsWorkflow
) : ViewModel() {
    @OptIn(WorkflowUiExperimentalApi::class)
    val rendering: Flow<StoryDetailsRendering> = renderWorkflowIn(
        workflow = storyDetailsWorkflow,
        scope = viewModelScope,
        props = MutableStateFlow(storyId),
    )

    @AssistedInject.Factory
    interface Factory {
        fun create(storyId: Long): StoryDetailsViewModel
    }
}
