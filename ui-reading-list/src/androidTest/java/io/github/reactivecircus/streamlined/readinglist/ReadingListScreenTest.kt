package io.github.reactivecircus.streamlined.readinglist

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.readinglist.di.ReadingListTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class ReadingListScreenTest : BaseScreenTest() {

    private val fragmentFactory = ReadingListTestAppComponent.getOrCreate().fragmentFactory

    @Test
    fun launchHeadlinesScreen_readingListDisplayed() {
        launchFragmentScenario<ReadingListFragment>(fragmentFactory)
        // TODO
    }
}
