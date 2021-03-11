package io.github.reactivecircus.streamlined.design.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun TopBar(title: String, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalElevationOverlay provides null) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.surface,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = if (MaterialTheme.colors.isLight) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.primary
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
            )
        }
    }
}

@Preview("light")
@Composable
fun PreviewTopBarLight() {
    StreamlinedTheme(darkTheme = false) {
        TopBar(title = "Streamlined.")
    }
}

@Preview("dark")
@Composable
fun PreviewTopBarDark() {
    StreamlinedTheme(darkTheme = true) {
        TopBar(title = "Streamlined.")
    }
}
