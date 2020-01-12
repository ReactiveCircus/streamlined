package io.github.reactivecircus.streamlined.headlines

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class HeadlinesScreenTest : BaseScreenTest() {

    @Test
    fun openHeadlinesScreen_headlinesDisplayed() {
        launchFragmentScenario<HeadlinesFragment>()
        // TODO
    }
}
