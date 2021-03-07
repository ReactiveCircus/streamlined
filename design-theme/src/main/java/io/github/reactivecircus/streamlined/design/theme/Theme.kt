package io.github.reactivecircus.streamlined.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun StreamlinedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) StreamlinedDarkColors else StreamlinedLightColors,
        typography = StreamlinedTypography,
        shapes = StreamlinedShapes,
        content = content,
    )
}
