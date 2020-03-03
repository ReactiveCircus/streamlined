package io.github.reactivecircus.streamlined.design

import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setDefaultBackgroundColor(): Snackbar {
    val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return takeIf { nightMode != Configuration.UI_MODE_NIGHT_YES }?.let {
        setBackgroundTint(ContextCompat.getColor(context, android.R.color.background_dark))
    } ?: this
}
