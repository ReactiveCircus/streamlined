package io.github.reactivecircus.streamlined.home

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.home.di.HomeTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class HomeScreenTest : BaseScreenTest() {

    private val fragmentFactory = HomeTestAppComponent.factory()
        .create(testingFrameworkComponent)
        .fragmentFactory

    @Test
    fun openHomeScreen_personalizedContentDisplayed() {
        launchFragmentScenario<HomeFragment>(factory = fragmentFactory)
        // TODO
    }
}
