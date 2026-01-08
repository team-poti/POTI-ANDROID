package com.poti.android.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.poti.android.R

val PotiBaseTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false,
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

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
    display20b = PotiBaseTextStyle.copy(
        fontFamily = PotiFontBold,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),
    display18b = PotiBaseTextStyle.copy(
        fontFamily = PotiFontBold,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),
    title18sb = PotiBaseTextStyle.copy(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),
    body16sb = PotiBaseTextStyle.copy(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body16m = PotiBaseTextStyle.copy(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body14sb = PotiBaseTextStyle.copy(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    body14m = PotiBaseTextStyle.copy(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    caption12m = PotiBaseTextStyle.copy(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    caption10m = PotiBaseTextStyle.copy(
        fontFamily = PotiFontMedium,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    button16sb = PotiBaseTextStyle.copy(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    button14sb = PotiBaseTextStyle.copy(
        fontFamily = PotiFontSemiBold,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
)

val LocalPotiTypographyProvider = staticCompositionLocalOf { defaultPotiTypography }
