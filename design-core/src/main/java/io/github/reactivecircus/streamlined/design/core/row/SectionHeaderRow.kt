package io.github.reactivecircus.streamlined.design.core.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun SectionHeaderRow(title: String, modifier: Modifier = Modifier) {
    @Suppress("DEPRECATION")
    val locale = LocalConfiguration.current.locale
    Box(modifier = modifier.background(MaterialTheme.colors.surface)) {
        Text(
            text = title.toUpperCase(locale),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview("light")
@Composable
fun PreviewSectionHeaderRowLight() {
    StreamlinedTheme(darkTheme = false) {
        SectionHeaderRow(title = "Top Headlines")
    }
}

@Preview("dark")
@Composable
fun PreviewSectionHeaderRowDark() {
    StreamlinedTheme(darkTheme = true) {
        SectionHeaderRow(title = "Top Headlines")
    }
}
