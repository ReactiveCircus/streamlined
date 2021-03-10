package io.github.reactivecircus.streamlined.ui.readinglist

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.ui.testing.BaseScreenTest
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
