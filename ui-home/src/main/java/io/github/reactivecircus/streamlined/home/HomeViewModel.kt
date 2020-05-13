package io.github.reactivecircus.streamlined.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class HomeViewModel @Inject constructor(
    private val homeStateMachine: HomeStateMachine
) : ViewModel() {

    private val mutableState = MutableStateFlow<HomeState>(HomeState.InFlight.Initial)

    val state: Flow<HomeState> get() = mutableState

    init {
        // TODO convert homeStateMachine.state directly to StateFlow with stateIn()
        homeStateMachine.state
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
