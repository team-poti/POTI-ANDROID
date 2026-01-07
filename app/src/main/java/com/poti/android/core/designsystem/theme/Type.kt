package com.poti.android.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.poti.android.R

val PotiFontBold = FontFamily(Font(R.font.pretendard_bold))
val PotiFontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
val PotiFontMedium = FontFamily(Font(R.font.pretendard_medium))

@Immutable
data class PotiTypography(
    val display20b: TextStyle,
    val display18b: TextStyle,

    val title18sb: TextStyle,

    val body16sb: TextStyle,
    val body16m: TextStyle,
    val body14sb: TextStyle,
    val body14m: TextStyle,

    val caption12m: TextStyle,
    val caption10m: TextStyle,

    val button16sb: TextStyle,
    val button14sb: TextStyle,
)

val defaultPotiTypography = PotiTypography(
    display20b = TextStyle(
        fontFamily = PotiFontBold,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),
    display18b = TextStyle(
        fontFamily = PotiFontBold,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),

    title18sb = TextStyle(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),

    body16sb = TextStyle(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body16m = TextStyle(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body14sb = TextStyle(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body14m = TextStyle(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),

    caption12m = TextStyle(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    caption10m = TextStyle(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),

    button16sb = TextStyle(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    button14sb = TextStyle(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
)

val LocalPotiTypographyProvider = staticCompositionLocalOf { defaultPotiTypography }
