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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@Composable
fun HeadlinesScreen() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color("#f6f9ff".toColorInt()),
                ) {
                    Text(
                        text = stringResource(R.string.title_headlines),
                        color = Color("#2e2858".toColorInt()),
                        fontFamily = FontFamily(Font(ThemeResource.font.fjalla_one)),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),
                    )
                }
            },
        ) {
            HeadlinesContent(Modifier.background(Color("#f6f9ff".toColorInt())))
        }
        // TODO add StreamlinedTheme to :design-theme
        // TODO add StreamlinedTopBar etc to :design-core
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
