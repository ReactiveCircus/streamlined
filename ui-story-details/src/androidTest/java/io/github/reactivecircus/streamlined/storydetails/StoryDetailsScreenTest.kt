package io.github.reactivecircus.streamlined.storydetails

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class StoryDetailsScreenTest : BaseScreenTest() {

    @Test
    fun openStoryDetailsScreen_storyDisplayed() {
        launchFragmentScenario<StoryDetailsFragment>()
        // TODO
    }
}
