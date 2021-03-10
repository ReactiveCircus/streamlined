package io.github.reactivecircus.streamlined.ui.testing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeResource.style.Theme_Streamlined_DayNight)
        super.onCreate(savedInstanceState)
    }
}
