package io.github.reactivecircus.streamlined.headlines

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.headlines.di.HeadlinesTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class HeadlinesScreenTest : BaseScreenTest() {

    private val fragmentFactory = HeadlinesTestAppComponent.factory()
        .create(testingFrameworkComponent)
        .fragmentFactory

    @Test
    fun openHeadlinesScreen_headlinesDisplayed() {
        launchFragmentScenario<HeadlinesFragment>(fragmentFactory)
        // TODO
    }
}
