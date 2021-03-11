package io.github.reactivecircus.streamlined.design.core.row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun EmptyPlaceholderRow(
    emptyMessage: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = emptyMessage,
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle2,
        )
    }
}

@Preview("light")
@Composable
fun PreviewEmptyPlaceholderRowLight() {
    StreamlinedTheme(darkTheme = false) {
        EmptyPlaceholderRow(emptyMessage = "No results found.")
    }
}

@Preview("dark")
@Composable
fun PreviewEmptyPlaceholderRowDark() {
    StreamlinedTheme(darkTheme = true) {
        EmptyPlaceholderRow(emptyMessage = "No results found.")
    }
}
