package io.github.reactivecircus.streamlined.navigator

import android.app.Activity

/**
 * APIs for performing cross-module in-app navigation.
 */
interface Navigator {

    fun navigateToStoryDetailsScreen(activity: Activity, storyId: String)
}
