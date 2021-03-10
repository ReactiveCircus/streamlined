package io.github.reactivecircus.streamlined.design.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val FiraSans = FontFamily(
    Font(R.font.fira_sans_condensed_medium, FontWeight.Medium),
    Font(R.font.fira_sans_condensed_bold, FontWeight.Bold),
)

private val FjallaOne = FontFamily(
    Font(R.font.fjalla_one)
)

internal val StreamlinedTypography = Typography(
    defaultFontFamily = FiraSans,
    h1 = TextStyle(
        fontFamily = FjallaOne,
        fontSize = 96.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 117.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = FjallaOne,
        fontSize = 60.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 73.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = FjallaOne,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 59.sp
    ),
    h4 = TextStyle(
        fontFamily = FjallaOne,
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 37.sp
    ),
    h5 = TextStyle(
        fontFamily = FjallaOne,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 29.sp
    ),
    h6 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    )
)
