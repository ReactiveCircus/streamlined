package io.github.reactivecircus.streamlined.storydetails

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.navigator.createNavArgsBundle
import io.github.reactivecircus.streamlined.navigator.input.StoryDetailsInput
import io.github.reactivecircus.streamlined.storydetails.di.StoryDetailsTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class StoryDetailsScreenTest : BaseScreenTest() {

    private val fragmentFactory = StoryDetailsTestAppComponent.getOrCreate().fragmentFactory

    private val args = createNavArgsBundle(StoryDetailsInput(storyId = 3))

    @Test
    fun launchStoryDetailsScreen_storyDisplayed() {
        launchFragmentScenario<StoryDetailsFragment>(fragmentFactory, args)
    }
}
