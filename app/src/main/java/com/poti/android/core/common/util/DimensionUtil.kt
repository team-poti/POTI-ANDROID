package com.poti.android.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val figmaScreenWidth = 360.dp
private val figmaScreenHeight = 800.dp

@Composable
fun screenHeightDp(height: Dp): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val ratio = screenHeight / figmaScreenHeight
    return height * ratio
}

@Composable
fun screenWidthDp(width: Dp): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val ratio = screenWidth / figmaScreenWidth
    return width * ratio
}
