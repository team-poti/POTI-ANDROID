package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ActionButtonType = ActionButtonType.PRIMARY,
    level: ActionButtonLevel = ActionButtonLevel.MAIN,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when (type) {
        ActionButtonType.PRIMARY -> {
            when (level) {
                ActionButtonLevel.MAIN ->
                    if (isPressed) PotiTheme.colors.poti800 else PotiTheme.colors.poti600
                ActionButtonLevel.SUB ->
                    if (isPressed) PotiTheme.colors.gray300 else PotiTheme.colors.gray100
            }
        }

        ActionButtonType.SECONDARY -> {
            when (level) {
                ActionButtonLevel.MAIN ->
                    if (isPressed) PotiTheme.colors.gray900 else PotiTheme.colors.black
                ActionButtonLevel.SUB ->
                    if (isPressed) PotiTheme.colors.gray300 else PotiTheme.colors.gray100
            }
        }

        ActionButtonType.DEACTIVE -> {
            when (level) {
                ActionButtonLevel.MAIN -> PotiTheme.colors.gray700
                ActionButtonLevel.SUB -> PotiTheme.colors.gray100
            }
        }
    }

    val contentColor = when (type) {
        ActionButtonType.PRIMARY ->
            if (level == ActionButtonLevel.MAIN) PotiTheme.colors.white else PotiTheme.colors.poti600
        ActionButtonType.SECONDARY ->
            if (level == ActionButtonLevel.MAIN) PotiTheme.colors.white else PotiTheme.colors.gray900
        ActionButtonType.DEACTIVE ->
            if (level == ActionButtonLevel.MAIN) PotiTheme.colors.white else PotiTheme.colors.gray700
    }

    Row(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
            .then(
                if (enabled && type != ActionButtonType.DEACTIVE) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
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

enum class ActionButtonType {
    PRIMARY,
    SECONDARY,
    DEACTIVE,
}

enum class ActionButtonLevel {
    MAIN,
    SUB,
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
                type = ActionButtonType.PRIMARY,
                level = ActionButtonLevel.MAIN,
            )

            PotiActionButton(
                text = "Primary Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.PRIMARY,
                level = ActionButtonLevel.SUB,
            )

            PotiActionButton(
                text = "Secondary Main",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.SECONDARY,
                level = ActionButtonLevel.MAIN,
            )

            PotiActionButton(
                text = "Secondary Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.SECONDARY,
                level = ActionButtonLevel.SUB,
            )

            PotiActionButton(
                text = "Deactive Main",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.DEACTIVE,
                level = ActionButtonLevel.MAIN,
            )

            PotiActionButton(
                text = "Deactive Sub",
                onClick = {},
                modifier = Modifier.width(328.dp),
                type = ActionButtonType.DEACTIVE,
                level = ActionButtonLevel.SUB,
            )
        }
    }
}
