package io.github.reactivecircus.streamlined.home

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class HomeScreenTest : BaseScreenTest() {

    @Test
    fun openHomeScreen_feedsDisplayed() {
        launchFragmentScenario<HomeFragment>()
        // TODO
    }
}
