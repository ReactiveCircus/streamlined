package io.github.reactivecircus.streamlined.home

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.home.di.HomeTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import io.github.reactivecircus.streamlined.testing.assumption.assumeNoPersistedHeadlineStories
import io.github.reactivecircus.streamlined.testing.assumption.assumeNoPersistedPersonalizedStories
import org.junit.Test

@LargeTest
class HomeScreenTest : BaseScreenTest() {

    private val fragmentFactory = HomeTestAppComponent.getOrCreate().fragmentFactory

    override fun setUp() {
        super.setUp()
        assumeNoPersistedHeadlineStories()
        assumeNoPersistedPersonalizedStories()
    }

    @Test
    fun openHomeScreen_headlinesAndPersonalizedContentDisplayed() {
        launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
        // TODO
    }
}
