package com.poti.android.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object PotiTheme {
    val colors: PotiColors
        @Composable
        @ReadOnlyComposable
        get() = LocalPotiColorsProvider.current
    val typography: PotiTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPotiTypographyProvider.current
}

@Composable
fun ProvidePotiColorsAndTypography(
    colors: PotiColors,
    typography: PotiTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPotiColorsProvider provides colors,
        LocalPotiTypographyProvider provides typography,
        content = content,
    )
}

@Composable
fun PotiTheme(
    content: @Composable () -> Unit,
) {
    ProvidePotiColorsAndTypography(
        colors = defaultPotiColors,
        typography = defaultPotiTypography,
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.run {
                    WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
                }
            }
        }
        MaterialTheme(
            content = content
        )
    }
}
