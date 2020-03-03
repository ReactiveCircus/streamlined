package io.github.reactivecircus.streamlined

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.reactivecircus.streamlined.design.setDefaultTaskBarColor
import io.github.reactivecircus.streamlined.design.R as ThemeResource

class StreamlinedActivity : AppCompatActivity(R.layout.activity_streamlined) {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeResource.style.Theme_Streamlined_DayNight)
        setDefaultTaskBarColor()
        supportFragmentManager.fragmentFactory = appComponent.fragmentFactory
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            appComponent.screenNameNotifier, true
        )
        super.onCreate(savedInstanceState)
    }
}
