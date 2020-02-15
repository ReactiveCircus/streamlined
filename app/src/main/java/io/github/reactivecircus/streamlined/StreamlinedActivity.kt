package io.github.reactivecircus.streamlined

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.reactivecircus.streamlined.ui.R as ThemeR

class StreamlinedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = appComponent.fragmentFactory
        super.onCreate(savedInstanceState)
        setTheme(ThemeR.style.Theme_Streamlined_DayNight)
        setContentView(R.layout.activity_streamlined)
    }
}
