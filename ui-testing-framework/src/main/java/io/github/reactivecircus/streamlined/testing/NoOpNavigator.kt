package io.github.reactivecircus.streamlined.testing

import android.app.Activity
import io.github.reactivecircus.streamlined.navigator.Navigator
import javax.inject.Inject

/**
 * No-op implementation of [Navigator].
 */
class NoOpNavigator @Inject constructor() : Navigator {

    override fun navigateToStoryDetailsScreen(activity: Activity, storyId: Long) = Unit
}
