package io.github.reactivecircus.streamlined.readinglist

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
@HiltAndroidTest
class ReadingListScreenTest : BaseScreenTest() {

    @Test
    fun launchHeadlinesScreen_readingListDisplayed() {
        launchFragmentInTest<ReadingListFragment>()
        // TODO
    }
}
