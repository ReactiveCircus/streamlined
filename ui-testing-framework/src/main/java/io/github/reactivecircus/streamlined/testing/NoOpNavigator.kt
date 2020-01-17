package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.navigator.Navigator

/**
 * No-op implementation of [Navigator].
 */
class NoOpNavigator : Navigator {

    override fun navigateToStoryDetailsScreen(storyId: String) = Unit
}
