package io.github.reactivecircus.streamlined

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment
import io.github.reactivecircus.streamlined.navigator.Navigator
import javax.inject.Inject

/**
 * Implementation of [Navigator].
 */
// TODO bound this to an ActivityScope (MainActivity)
class StreamlinedNavigator @Inject constructor(
    private val activity: Activity
) : Navigator {

    override fun navigateToStoryDetailsScreen(storyId: String) {
        activity.findNavController(R.id.rootNavHostFragment).navigate(
            R.id.action_global_storyDetailsFragment,
            bundleOf(StoryDetailsFragment.ARG_STORY_ID to storyId)
        )
    }
}
