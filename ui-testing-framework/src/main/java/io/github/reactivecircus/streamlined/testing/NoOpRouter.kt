package io.github.reactivecircus.streamlined.testing

import io.github.reactivecircus.streamlined.router.Router

/**
 * No-op implementation of [Router].
 */
class NoOpRouter : Router {

    override fun navigateToStoryDetailsScreen(storyId: String) = Unit
}
