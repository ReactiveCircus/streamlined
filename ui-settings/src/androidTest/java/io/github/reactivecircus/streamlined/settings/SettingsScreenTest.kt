package io.github.reactivecircus.streamlined.settings

import androidx.test.filters.LargeTest
import io.github.reactivecircus.streamlined.settings.di.SettingsTestAppComponent
import io.github.reactivecircus.streamlined.testing.BaseScreenTest
import org.junit.Test

@LargeTest
class SettingsScreenTest : BaseScreenTest() {

    private val fragmentFactory = SettingsTestAppComponent.factory()
        .create(testingFrameworkComponent)
        .fragmentFactory

    @Test
    fun openSettingsScreen_settingsDisplayed() {
        launchFragmentScenario<SettingsFragment>(fragmentFactory)
        // TODO
    }
}
