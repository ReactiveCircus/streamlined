package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeWorkflow: HomeWorkflow
) : ViewModel() {
    @OptIn(WorkflowUiExperimentalApi::class)
    val rendering: Flow<HomeRendering> = renderWorkflowIn(
        workflow = homeWorkflow,
        scope = viewModelScope,
    )
}
