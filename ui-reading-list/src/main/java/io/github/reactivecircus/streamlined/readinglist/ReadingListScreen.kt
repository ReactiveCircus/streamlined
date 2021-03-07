package io.github.reactivecircus.streamlined.readinglist

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
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@Composable
fun ReadingListScreen() {
    MaterialTheme {
        ProvideWindowInsets {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color("#f6f9ff".toColorInt()),
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        Text(
                            text = stringResource(R.string.title_reading_list),
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
                }
            ) {
                ReadingListContent(Modifier.background(Color("#f6f9ff".toColorInt())))
            }
            // TODO add StreamlinedTheme to :design-theme
            // TODO add TopBar (InsetAware) etc to :design-core
        }
    }
}

@Composable
fun ReadingListContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Reading list",
            modifier = modifier.padding(24.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun ReadingListScreenPreview() {
    ReadingListScreen()
}
