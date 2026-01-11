package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun PotiEmptyImagePlaceholder(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFF0F0F0)
) {
    Canvas(modifier = modifier) {
        // 배경
        drawRect(color = color)

        val step = 20.dp.toPx()
        val checkColor = Color(0xFFE0E0E0)

        for (x in 0 until (size.width / step).toInt() + 1) {
            for (y in 0 until (size.height / step).toInt() + 1) {
                if ((x + y) % 2 == 1) {
                    drawRect(
                        color = checkColor,
                        topLeft = Offset(x * step, y * step),
                        size = Size(step, step)
                    )
                }
            }
        }
    }
}
