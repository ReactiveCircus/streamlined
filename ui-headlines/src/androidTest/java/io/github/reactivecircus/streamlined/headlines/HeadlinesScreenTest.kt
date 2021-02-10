package io.github.reactivecircus.streamlined.headlines

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
@HiltAndroidTest
class HeadlinesScreenTest : BaseScreenTest() {

    @Test
    fun launchHeadlinesScreen_headlinesDisplayed() {
        launchFragmentInTest<HeadlinesFragment>()
        // TODO
    }
}
