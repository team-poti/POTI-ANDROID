package com.poti.android.core.common.extension

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit,
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = this.clickable(
    interactionSource = interactionSource,
    indication = null,
    enabled = enabled,
    onClick = onClick,
)

fun Modifier.roundedBackgroundWithBorder(
    cornerRadius: Dp,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
): Modifier {
    return this
        .clip(RoundedCornerShape(cornerRadius))
        .background(backgroundColor)
        .border(
            width = borderWidth,
            color = borderColor,
            shape = RoundedCornerShape(cornerRadius),
        )
}

@Composable
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    offsetX: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    blur: Dp = 1.dp,
    spread: Dp = 1.dp,
) = composed {
    val density = LocalDensity.current

    val paint = remember(color, blur) {
        Paint().apply {
            this.color = color
            val blurPx = with(density) { blur.toPx() }
            if (blurPx > 0f) {
                this.asFrameworkPaint().maskFilter =
                    BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
        }
    }

    drawBehind {
        val spreadPx = spread.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val shadowWidth = size.width + spreadPx
        val shadowHeight = size.height + spreadPx

        if (shadowWidth <= 0f || shadowHeight <= 0f) return@drawBehind

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetXPx, offsetYPx)
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}

fun Modifier.topRoundedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp,
) = this.drawBehind {
    val strokeWidthPx = strokeWidth.toPx()
    val radiusPx = cornerRadius.toPx()
    val halfStroke = strokeWidthPx / 2

    val path = Path().apply {
        moveTo(x = halfStroke, y = radiusPx)

        quadraticTo(x1 = halfStroke, y1 = halfStroke, x2 = radiusPx, y2 = halfStroke)

        lineTo(x = size.width - radiusPx, y = halfStroke)

        quadraticBezierTo(
            x1 = size.width - halfStroke,
            y1 = halfStroke,
            x2 = size.width - halfStroke,
            y2 = radiusPx,
        )
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidthPx),
    )
}

fun Modifier.bottomBorder(
    strokeWidth: Dp,
    color: Color,
    isVisible: Boolean = true,
): Modifier {
    if (!isVisible) return this

    return this.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width, y = height),
            strokeWidth = strokeWidthPx,
        )
    }
}
