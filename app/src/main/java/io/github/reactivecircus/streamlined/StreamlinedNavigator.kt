package io.github.reactivecircus.streamlined

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment

/**
 * Implementation of [Navigator].
 */
class StreamlinedNavigator(private val activityProvider: () -> Activity) : Navigator {

    override fun navigateToStoryDetailsScreen(storyId: Long) {
        activityProvider().findNavController(R.id.rootNavHostFragment).navigate(
            R.id.action_global_storyDetailsFragment,
            bundleOf(StoryDetailsFragment.ARG_STORY_ID to storyId)
        )
    }
}
