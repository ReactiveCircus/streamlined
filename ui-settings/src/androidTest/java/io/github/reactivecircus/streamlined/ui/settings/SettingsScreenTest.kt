package io.github.reactivecircus.streamlined.ui.settings

import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.reactivecircus.streamlined.ui.testing.BaseScreenTest
import org.junit.Test

@LargeTest
@HiltAndroidTest
class SettingsScreenTest : BaseScreenTest() {

    @Test
    fun launchSettingsScreen_settingsDisplayed() {
        launchFragmentInTest<SettingsFragment>()
        // TODO
    }
}
