package io.github.reactivecircus.streamlined.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import io.github.reactivecircus.streamlined.design.core.TopBar
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun HeadlinesScreen() {
    StreamlinedTheme {
        ProvideWindowInsets {
            Scaffold(
                topBar = {
                    TopBar(
                        title = stringResource(R.string.title_headlines),
                        modifier = Modifier.statusBarsPadding(),
                    )
                },
            ) {
                HeadlinesContent(Modifier.background(MaterialTheme.colors.background))
            }
        }
    }
}

@Composable
fun HeadlinesContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Headlines",
            modifier = modifier.padding(24.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun PreviewHeadlinesScreen() {
    HeadlinesScreen()
}
