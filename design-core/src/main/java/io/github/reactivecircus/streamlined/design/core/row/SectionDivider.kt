package io.github.reactivecircus.streamlined.design.core.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun SectionDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(Thickness)
            .background(color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha))
    )
}

private val Thickness = 8.dp
private const val DividerAlpha = 0.12f

@Preview("light")
@Composable
private fun PreviewSectionDividerLight() {
    StreamlinedTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            SectionDivider()
        }
    }
}

@Preview("dark")
@Composable
private fun PreviewSectionDividerDark() {
    StreamlinedTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            SectionDivider()
        }
    }
}
