package io.github.reactivecircus.streamlined.home

import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
interface HomeUiConfigs {
    val numberOfHeadlinesDisplayed: Int
    val transientErrorDisplayDuration: Duration
    val delayDispatcher: CoroutineDispatcher
}

@ExperimentalTime
class DefaultHomeUiConfigs @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : HomeUiConfigs {
    override val numberOfHeadlinesDisplayed: Int = NUMBER_OF_HEADLINES_DISPLAYED
    override val transientErrorDisplayDuration: Duration = TRANSIENT_ERROR_DISPLAY_DURATION
    override val delayDispatcher: CoroutineDispatcher = coroutineDispatcherProvider.computation

    companion object {
        internal const val NUMBER_OF_HEADLINES_DISPLAYED = 3
        internal val TRANSIENT_ERROR_DISPLAY_DURATION = 2.seconds
    }
}
