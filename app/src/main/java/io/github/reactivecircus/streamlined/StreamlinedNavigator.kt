package io.github.reactivecircus.streamlined

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment
import javax.inject.Inject

/**
 * Implementation of [Navigator].
 */
class StreamlinedNavigator @Inject constructor() : Navigator {

    override fun navigateToStoryDetailsScreen(activity: Activity, storyId: String) {
        activity.findNavController(R.id.rootNavHostFragment).navigate(
            R.id.action_global_storyDetailsFragment,
            bundleOf(StoryDetailsFragment.ARG_STORY_ID to storyId)
        )
    }
}
