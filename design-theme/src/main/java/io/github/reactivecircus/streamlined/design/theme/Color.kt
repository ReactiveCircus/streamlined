package io.github.reactivecircus.streamlined.design.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Denim = Color(0xff1565c0)
private val Paua = Color(0xff2e2858)
private val AliceBlue = Color(0xfff6f9ff)
private val Carmine = Color(0xffb00020)
private val Malibu = Color(0xff4fc3f7)
private val PacificBlue = Color(0xff0093c4)

internal val StreamlinedLightColors = lightColors(
    primary = Denim,
    primaryVariant = Denim,
    secondary = Paua,
    secondaryVariant = Paua,
    background = AliceBlue,
    surface = AliceBlue,
    error = Carmine,
    onPrimary = Color.White,
    onSecondary = AliceBlue,
    onBackground = Paua,
    onSurface = Paua,
    onError = Color.White,
)

internal val StreamlinedDarkColors = darkColors(
    primary = Malibu,
    primaryVariant = PacificBlue,
    secondary = AliceBlue,
    secondaryVariant = AliceBlue,
    background = Paua,
    surface = Paua,
    error = Carmine,
    onPrimary = Paua,
    onSecondary = Paua,
    onBackground = AliceBlue,
    onSurface = AliceBlue,
    onError = Paua,
)
