package io.github.reactivecircus.streamlined.storydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.squareup.workflow1.renderWorkflowIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class StoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val storyId: Long,
    storyDetailsWorkflow: StoryDetailsWorkflow
) : ViewModel() {
    val rendering: Flow<StoryDetailsRendering> = renderWorkflowIn(
        storyDetailsWorkflow, viewModelScope, MutableStateFlow(storyId)
    ) {}.mapLatest { it.rendering }

    @AssistedInject.Factory
    interface Factory {
        fun create(storyId: Long): StoryDetailsViewModel
    }
}
