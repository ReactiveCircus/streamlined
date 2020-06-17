package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.workflow1.renderWorkflowIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    homeWorkflow: HomeWorkflow
) : ViewModel() {
    val rendering: Flow<HomeRendering> = renderWorkflowIn(
        homeWorkflow, viewModelScope, MutableStateFlow(Unit)
    ) {}.mapLatest { it.rendering }
}
