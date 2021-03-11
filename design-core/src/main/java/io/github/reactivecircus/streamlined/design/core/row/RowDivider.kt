package io.github.reactivecircus.streamlined.design.core.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun RowDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.padding(horizontal = 16.dp)
    )
}

@Preview("light")
@Composable
fun PreviewRowDividerLight() {
    StreamlinedTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            RowDivider()
        }
    }
}

@Preview("dark")
@Composable
fun PreviewRowDividerDark() {
    StreamlinedTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(24.dp),
            contentAlignment = Alignment.Center
        ) {
            RowDivider()
        }
    }
}
