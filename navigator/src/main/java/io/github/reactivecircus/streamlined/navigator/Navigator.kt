package io.github.reactivecircus.streamlined.navigator

/**
 * APIs for performing cross-module in-app navigation.
 */
interface Navigator {

    fun navigateToStoryDetailsScreen(storyId: Long)

    fun navigateToHeadlinesScreen()
}
