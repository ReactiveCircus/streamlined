package io.github.reactivecircus.streamlined.headlines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun HeadlinesScreen() {
    StreamlinedTheme {
        ProvideWindowInsets {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        Text(
                            text = stringResource(R.string.title_headlines),
                            style = MaterialTheme.typography.h5,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically),
                        )
                    }
                },
            ) {
                HeadlinesContent(Modifier.background(MaterialTheme.colors.background))
            }
            // TODO add StreamlinedTheme to :design-theme
            // TODO add TopBar (InsetAware) etc to :design-core
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
private fun HeadlinesScreenPreview() {
    HeadlinesScreen()
}
