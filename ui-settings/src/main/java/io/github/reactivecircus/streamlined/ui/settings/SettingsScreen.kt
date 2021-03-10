package io.github.reactivecircus.streamlined.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import io.github.reactivecircus.streamlined.design.core.NightModeSwitch
import io.github.reactivecircus.streamlined.design.core.TopBar
import io.github.reactivecircus.streamlined.design.theme.StreamlinedTheme

@Composable
fun SettingsScreen() {
    val nightMode = remember { mutableStateOf(false) }
    StreamlinedTheme(darkTheme = nightMode.value) {
        ProvideWindowInsets {
            Scaffold(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding(bottom = false),
                topBar = {
                    TopBar(
                        title = stringResource(R.string.title_settings)
                    )
                }
            ) {
                SettingsContent(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onNightModeChange = { isOn ->
                        nightMode.value = isOn
                    }
                )
            }
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
private fun PreviewSettingsScreen() {
    SettingsScreen()
}
