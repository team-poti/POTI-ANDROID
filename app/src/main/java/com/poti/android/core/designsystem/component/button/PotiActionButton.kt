package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiColors
import com.poti.android.core.designsystem.theme.PotiTheme

enum class ActionButtonType {
    PRIMARY_MAIN,
    PRIMARY_SUB,
    SECONDARY_MAIN,
    SECONDARY_SUB,
    DEACTIVE_MAIN,
    DEACTIVE_SUB,
    ;

    fun resolve(enabled: Boolean): ActionButtonType {
        if (enabled) return this

        return when (this) {
            PRIMARY_MAIN, SECONDARY_MAIN -> DEACTIVE_MAIN
            PRIMARY_SUB, SECONDARY_SUB -> DEACTIVE_SUB
            else -> this
        }
    }

    fun getBackgroundColor(
        colors: PotiColors,
        isPressed: Boolean,
    ): Color =
        when (this) {
            PRIMARY_MAIN -> if (isPressed) colors.poti800 else colors.poti600
            PRIMARY_SUB -> if (isPressed) colors.gray300 else colors.gray100
            SECONDARY_MAIN -> if (isPressed) colors.gray900 else colors.black
            SECONDARY_SUB -> if (isPressed) colors.gray300 else colors.gray100
            DEACTIVE_MAIN -> colors.gray700
            DEACTIVE_SUB -> colors.gray100
        }

    fun getContentColor(colors: PotiColors): Color {
        return when (this) {
            PRIMARY_MAIN, SECONDARY_MAIN, DEACTIVE_MAIN -> colors.white
            PRIMARY_SUB -> colors.poti600
            SECONDARY_SUB -> colors.gray900
            DEACTIVE_SUB -> colors.gray700
        }
    }

    val isInteractive: Boolean
        get() = this != DEACTIVE_MAIN && this != DEACTIVE_SUB
}

@Composable
fun PotiActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ActionButtonType = ActionButtonType.PRIMARY_MAIN,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = PotiTheme.colors

    val currentType = type.resolve(enabled)

    val backgroundColor = currentType.getBackgroundColor(colors, isPressed)
    val contentColor = currentType.getContentColor(colors)
    val isButtonEnabled = enabled && currentType.isInteractive

    Row(
        modifier = modifier
            .heightIn(min = 52.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .noRippleClickable(
                interactionSource = interactionSource,
                enabled = isButtonEnabled,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = PotiTheme.typography.button16sb,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiActionButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiActionButton(
                text = "Primary Main",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.PRIMARY_MAIN,
            )

            PotiActionButton(
                text = "Primary Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.PRIMARY_SUB,
            )

            PotiActionButton(
                text = "Secondary Main",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.SECONDARY_MAIN,
            )

            PotiActionButton(
                text = "Secondary Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.SECONDARY_SUB,
            )

            PotiActionButton(
                text = "Deactive Main",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.DEACTIVE_MAIN,
            )

            PotiActionButton(
                text = "Deactive Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.DEACTIVE_SUB,
            )
        }
    }
}
