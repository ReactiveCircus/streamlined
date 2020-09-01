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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.streamlined.design.core.NightModeSwitch
import io.github.reactivecircus.streamlined.design.R as ThemeResource

@Composable
fun SettingsScreen() {
    val nightMode = remember { mutableStateOf(false) }
    MaterialTheme(
        colors = if (nightMode.value) darkColors() else lightColors()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.surface,
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
        // TODO add StreamlinedTopBar etc to :design-core
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
