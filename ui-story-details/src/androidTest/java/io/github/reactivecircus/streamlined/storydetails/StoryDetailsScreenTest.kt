package io.github.reactivecircus.streamlined.storydetails

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.navigator.createNavArgsBundle
import io.github.reactivecircus.streamlined.navigator.input.StoryDetailsInput
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
@HiltAndroidTest
class StoryDetailsScreenTest : BaseScreenTest() {

    private val args = createNavArgsBundle(StoryDetailsInput(storyId = 3))

    @Test
    fun launchStoryDetailsScreen_storyDisplayed() {
        launchFragmentInTest<StoryDetailsFragment>(args)
    }
}
