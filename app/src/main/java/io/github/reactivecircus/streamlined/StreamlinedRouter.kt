package io.github.reactivecircus.streamlined

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment
import reactivecircus.streamlined.router.Router
import javax.inject.Inject

/**
 * Implementation of [Router].
 */
// TODO bound this to an ActivityScope (MainActivity)
class StreamlinedRouter @Inject constructor(
    private val activity: Activity
) : Router {

    override fun navigateToStoryDetailsScreen(storyId: String) {
        activity.findNavController(R.id.rootNavHostFragment).navigate(
            R.id.action_global_storyDetailsFragment,
            bundleOf(StoryDetailsFragment.ARG_STORY_ID to storyId)
        )
    }
}
