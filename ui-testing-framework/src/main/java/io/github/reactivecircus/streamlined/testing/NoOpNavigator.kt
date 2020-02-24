package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.navigator.Navigator

/**
 * No-op implementation of [Navigator].
 */
object NoOpNavigator : Navigator {

    override fun navigateToStoryDetailsScreen(storyId: Long) = Unit

    override fun navigateToHeadlinesScreen() = Unit
}
