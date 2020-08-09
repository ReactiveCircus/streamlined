package io.github.reactivecircus.streamlined.storydetails

import androidx.core.os.bundleOf
import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.storydetails.di.StoryDetailsTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class StoryDetailsScreenTest : BaseScreenTest() {

    private val fragmentFactory = StoryDetailsTestAppComponent.getOrCreate().fragmentFactory

    private val args = bundleOf(StoryDetailsFragment.ARG_STORY_ID to 3)

    @Test
    fun launchStoryDetailsScreen_storyDisplayed() {
        launchFragmentScenario<StoryDetailsFragment>(fragmentFactory, args)
    }
}
