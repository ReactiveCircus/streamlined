package io.github.reactivecircus.streamlined.design.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.streamlined.design.foundation.IconSwitch
import io.github.reactivecircus.streamlined.design.foundation.IconSwitchConstants

@Composable
fun NightModeSwitch(
    isOn: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconSwitch(
        checked = isOn,
        onCheckedChange = onChange,
        modifier = modifier,
        colors = IconSwitchConstants.defaultColors(
            checkedThumbColor = CheckedThumbColor,
            checkedTrackColor = MaterialTheme.colors.background,
            checkedTrackAlpha = 1.0f,
            checkedBorderColor = CheckedThumbColor,
            checkedBorderAlpha = CheckedBorderAlpha,
            uncheckedThumbColor = MaterialTheme.colors.onBackground,
            uncheckedTrackColor = MaterialTheme.colors.surface,
            uncheckedTrackAlpha = 1.0f,
            uncheckedBorderColor = MaterialTheme.colors.onBackground,
            uncheckedBorderAlpha = UncheckedBorderAlpha,
        ),
        sizes = IconSwitchConstants.defaultSizes(
            trackWidth = 60.dp,
            trackHeight = 32.dp,
            thumbDiameter = 40.dp,
        )
    ) {
        MoonIcon(
            modifier = Modifier.size(MoonIconSize),
            tint = if (isOn) MaterialTheme.colors.onSurface else UncheckedIconColor,
        )
    }
}

private val CheckedThumbColor = Color(0xFF6E40C9)
private val UncheckedIconColor = Color(0xFFFFDF5D)

private val MoonIconSize = 16.dp
private const val CheckedBorderAlpha = 0.5f
private const val UncheckedBorderAlpha = 0.1f

@Composable
private fun MoonIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Canvas(
        modifier = modifier.aspectRatio(1f)
    ) {
        val sizePx = size.width

        drawContext.transform.rotate(-45f)
        drawContext.canvas.withSaveLayer(
            bounds = drawContext.size.toRect(),
            paint = Paint()
        ) {
            drawCircle(
                color = tint,
                radius = sizePx * 0.5f,
            )

            drawCircle(
                color = Color.Black,
                radius = sizePx * 0.4f,
                center = Offset(
                    x = size.width * 0.5f,
                    y = size.height * 0.20f,
                ),
                blendMode = BlendMode.DstOut
            )
        }
    }
}

@Preview("off", widthDp = 80, heightDp = 50)
@Composable
fun NightModeSwitchPreviewOff() {
    MaterialTheme(colors = lightColors()) {
        NightModeSwitch(
            isOn = false,
            onChange = {},
        )
    }
}

@Preview("on", widthDp = 80, heightDp = 50)
@Composable
fun NightModeSwitchPreviewOn() {
    MaterialTheme(colors = darkColors()) {
        NightModeSwitch(
            isOn = true,
            onChange = {},
        )
    }
}
