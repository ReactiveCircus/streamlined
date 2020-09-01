package io.github.reactivecircus.streamlined

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.design.setDefaultTaskBarColor
import javax.inject.Inject
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@AndroidEntryPoint
class StreamlinedActivity : AppCompatActivity(R.layout.activity_streamlined) {

    @Inject
    lateinit var screenNameNotifier: ScreenNameNotifier

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeResource.style.Theme_Streamlined_DayNight)
        setDefaultTaskBarColor()
        super.onCreate(savedInstanceState)

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            screenNameNotifier,
            true
        )

        // TODO enable edge-to-edge layout and fix insets
    }
}
