package io.github.reactivecircus.streamlined

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.storydetails.StoryDetailsFragment

/**
 * Implementation of [Navigator].
 */
class StreamlinedNavigator(private val activityProvider: () -> AppCompatActivity) : Navigator {

    override fun navigateToStoryDetailsScreen(storyId: Long) {
        activityProvider().findNavController(R.id.rootNavHostFragment).navigate(
            R.id.action_global_storyDetailsFragment,
            bundleOf(StoryDetailsFragment.ARG_STORY_ID to storyId)
        )
    }

    override fun navigateToHeadlinesScreen() {
        activityProvider().findNavController(R.id.mainNavHostFragment)
            .navigate(R.id.headlinesFragment)
    }
}
