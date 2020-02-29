package io.github.reactivecircus.streamlined.design

import android.app.Activity
import android.app.ActivityManager
import android.os.Build
import android.util.TypedValue

fun Activity.setDefaultTaskBarColor() {
    val typedValue = TypedValue()
    theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        setTaskDescription(
            ActivityManager.TaskDescription(null, 0, typedValue.data)
        )
    } else {
        @Suppress("DEPRECATION")
        setTaskDescription(
            ActivityManager.TaskDescription(null, null, typedValue.data)
        )
    }
}
