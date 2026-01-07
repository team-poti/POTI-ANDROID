package com.poti.android.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Poti200 = Color(0xFFC5E9FF)
val Poti400 = Color(0xFF94D6FD)
val Poti600 = Color(0xFF2FB2FF)
val Poti800 = Color(0xFF20A1EE)

val Gray100 = Color(0xFFEEEFF3)
val Gray300 = Color(0xFFDDDFE6)
val Gray500 = Color(0xFFCDCFD6)
val Gray700 = Color(0xFFA3A5AD)
val Gray800 = Color(0xFF7C7C83)
val Gray900 = Color(0xFF505056)

val SementicRed = Color(0xFFFF634B)
val Black = Color(0xFF303030)
val BlackA40 = Color(0xFFA6A6A6)
val White = Color(0xFFFAFAFC)

@Immutable
data class PotiColors(
    val poti200: Color,
    val poti400: Color,
    val poti600: Color,
    val poti800: Color,
    val gray100: Color,
    val gray300: Color,
    val gray500: Color,
    val gray700: Color,
    val gray800: Color,
    val gray900: Color,
    val sementicRed: Color,
    val black: Color,
    val blackA40: Color,
    val white: Color,
)

val defaultPotiColors = PotiColors(
    poti200 = Poti200,
    poti400 = Poti400,
    poti600 = Poti600,
    poti800 = Poti800,
    gray100 = Gray100,
    gray300 = Gray300,
    gray500 = Gray500,
    gray700 = Gray700,
    gray800 = Gray800,
    gray900 = Gray900,
    sementicRed = SementicRed,
    black = Black,
    blackA40 = BlackA40,
    white = White,
)

val LocalPotiColorsProvider = staticCompositionLocalOf { defaultPotiColors }
