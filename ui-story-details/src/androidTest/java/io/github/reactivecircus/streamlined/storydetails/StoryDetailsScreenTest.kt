package io.github.reactivecircus.streamlined.storydetails

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.storydetails.di.StoryDetailsTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class StoryDetailsScreenTest : BaseScreenTest() {

    private val fragmentFactory = StoryDetailsTestAppComponent.getOrCreate().fragmentFactory

    @Test
    fun openStoryDetailsScreen_storyDisplayed() {
        // TODO pass in ARG_STORY_ID
        launchFragmentScenario<StoryDetailsFragment>(fragmentFactory)
    }
}
