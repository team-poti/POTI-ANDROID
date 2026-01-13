package com.poti.android.core.designsystem.component.button

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.dropShadow
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val transition = updateTransition(isPressed, label = "transition")

    val animatedScale by transition.animateFloat(
        transitionSpec = { tween(300, easing = FastOutSlowInEasing) },
        label = "scale",
    ) { pressed -> if (pressed) 0.93f else 1f }

    val animatedBackgroundColor by transition.animateColor(
        transitionSpec = { tween(300, easing = FastOutSlowInEasing) },
        label = "background",
    ) { pressed -> if (pressed) PotiTheme.colors.poti800 else PotiTheme.colors.poti600 }

    Box(
        modifier = modifier
            .size(56.dp)
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale,
            )
            .dropShadow(
                shape = CircleShape,
                color = Color.Black.copy(0.05f),
                offsetX = 0.dp,
                offsetY = 2.dp,
                blur = 4.dp,
                spread = 0.dp,
            )
            .clip(CircleShape)
            .background(animatedBackgroundColor)
            .noRippleClickable(
                interactionSource = interactionSource,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
            contentDescription = null,
            tint = PotiTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiFloatingButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PotiFloatingButton(
                onClick = {},
            )
        }
    }
}
