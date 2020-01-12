package io.github.reactivecircus.streamlined.readinglist

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class ReadingListScreenTest : BaseScreenTest() {

    @Test
    fun openHeadlinesScreen_headlinesDisplayed() {
        launchFragmentScenario<ReadingListFragment>()
        // TODO
    }
}
