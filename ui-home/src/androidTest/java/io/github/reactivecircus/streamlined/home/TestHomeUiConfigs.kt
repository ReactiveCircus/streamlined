@file:Suppress("DEPRECATION")

package io.github.reactivecircus.streamlined.home

import android.os.AsyncTask
import kotlinx.coroutines.asCoroutineDispatcher
import javax.inject.Inject
import kotlin.time.seconds

class TestHomeUiConfigs @Inject constructor() : HomeUiConfigs {

    override val numberOfHeadlinesDisplayed = DefaultHomeUiConfigs.NUMBER_OF_HEADLINES_DISPLAYED

    override val transientErrorDisplayDuration = 5.seconds

    override val delayDispatcher = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()
}
