package io.github.reactivecircus.streamlined.readinglist

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.readinglist.di.ReadingListTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class ReadingListScreenTest : BaseScreenTest() {

    private val fragmentFactory = ReadingListTestAppComponent.factory()
        .create(testingFrameworkComponent)
        .fragmentFactory

    @Test
    fun openHeadlinesScreen_readingListDisplayed() {
        launchFragmentScenario<ReadingListFragment>(fragmentFactory)
        // TODO
    }
}
