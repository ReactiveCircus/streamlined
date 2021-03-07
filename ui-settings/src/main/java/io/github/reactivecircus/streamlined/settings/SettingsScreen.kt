package io.github.reactivecircus.streamlined.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import io.github.reactivecircus.streamlined.design.core.NightModeSwitch
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@Composable
fun SettingsScreen() {
    val nightMode = remember { mutableStateOf(false) }
    MaterialTheme(
        colors = if (nightMode.value) darkColors().copy(
            primary = Color("#4fc3f7".toColorInt()),
            primaryVariant = Color("#0093c4".toColorInt()),
            secondary = Color("#f6f9ff".toColorInt()),
            secondaryVariant = Color("#f6f9ff".toColorInt()),
            background = Color("#2e2858".toColorInt()),
            surface = Color("#2e2858".toColorInt()),
            error = Color("#b00020".toColorInt()),
            onPrimary = Color("#2e2858".toColorInt()),
            onSecondary = Color("#2e2858".toColorInt()),
            onBackground = Color("#f6f9ff".toColorInt()),
            onSurface = Color("#f6f9ff".toColorInt()),
            onError = Color("#2e2858".toColorInt()),
        ) else lightColors().copy(
            primary = Color("#1565c0".toColorInt()),
            primaryVariant = Color("#1565c0".toColorInt()),
            secondary = Color("#2e2858".toColorInt()),
            secondaryVariant = Color("#2e2858".toColorInt()),
            background = Color("#f6f9ff".toColorInt()),
            surface = Color("#f6f9ff".toColorInt()),
            error = Color("#b00020".toColorInt()),
            onPrimary = Color("#ffffff".toColorInt()),
            onSecondary = Color("#f6f9ff".toColorInt()),
            onBackground = Color("#2e2858".toColorInt()),
            onSurface = Color("#2e2858".toColorInt()),
            onError = Color("#ffffff".toColorInt()),
        )
    ) {
        ProvideWindowInsets {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier.statusBarsPadding()
                    ) {
                        Text(
                            text = stringResource(R.string.title_settings),
                            color = MaterialTheme.colors.onSurface,
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
                SettingsContent(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onNightModeChange = { isOn ->
                        nightMode.value = isOn
                    }
                )
            }
            // TODO add StreamlinedTheme to :design-theme
            // TODO add TopBar (InsetAware) etc to :design-core
        }
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onNightModeChange: (Boolean) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        NightModeSwitch(
            isOn = !MaterialTheme.colors.isLight,
            onChange = onNightModeChange,
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
