package io.github.reactivecircus.streamlined.design.core.row

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun ButtonRow(
    onClick: () -> Unit,
    buttonLabel: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Text(
            text = buttonLabel,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview("light")
@Composable
private fun PreviewButtonRowLight() {
    StreamlinedTheme(darkTheme = false) {
        ButtonRow(buttonLabel = "Read more", onClick = {})
    }
}

@Preview("dark")
@Composable
private fun PreviewButtonRowDark() {
    StreamlinedTheme(darkTheme = true) {
        ButtonRow(buttonLabel = "Read more", onClick = {})
    }
}
