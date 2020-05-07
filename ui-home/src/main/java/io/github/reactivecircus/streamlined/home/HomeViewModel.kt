package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class HomeViewModel @Inject constructor(
    private val homeStateMachine: HomeStateMachine
) : ViewModel() {

    private val mutableState = MutableLiveData<HomeState>()

    val state: LiveData<HomeState> = mutableState

    init {
        homeStateMachine.state
            .distinctUntilChanged()
            .onEach { newState ->
                mutableState.value = newState
            }
            .catch { Timber.e(it, "state machine flow cancelled") }
            .launchIn(viewModelScope)
    }

    fun refreshHomeFeeds() {
        viewModelScope.launch {
            homeStateMachine.dispatch(HomeAction.Refresh)
        }
    }
}
