package io.github.reactivecircus.streamlined.testing

import android.app.Activity
import io.github.reactivecircus.streamlined.navigator.Navigator

/**
 * No-op implementation of [Navigator].
 */
class NoOpNavigator : Navigator {

    override fun navigateToStoryDetailsScreen(activity: Activity, storyId: String) = Unit
}
