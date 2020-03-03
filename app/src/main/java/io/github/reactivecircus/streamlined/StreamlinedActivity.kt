package io.github.reactivecircus.streamlined

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.reactivecircus.streamlined.design.setDefaultTaskBarColor
import io.github.reactivecircus.streamlined.design.R as ThemeResource

class StreamlinedActivity : AppCompatActivity(R.layout.activity_streamlined) {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = appComponent.fragmentFactory
        setTheme(ThemeResource.style.Theme_Streamlined_DayNight)
        setDefaultTaskBarColor()
        super.onCreate(savedInstanceState)
    }
}
