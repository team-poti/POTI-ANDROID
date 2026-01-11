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
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun PotiModalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ModalButtonType = ModalButtonType.MAIN,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val colors = PotiTheme.colors
    val backgroundColor = type.getBackgroundColor(colors, isPressed)
    val contentColor = type.getContentColor(colors)

    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
            .noRippleClickable(
                interactionSource = interactionSource,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
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

enum class ModalButtonType {
    MAIN,
    SUB_1,
    SUB_2,
    ;

    fun getBackgroundColor(
        colors: PotiColors,
        isPressed: Boolean,
    ): Color =
        when (this) {
            MAIN -> if (isPressed) colors.poti800 else colors.poti600
            SUB_1 -> if (isPressed) colors.gray300 else colors.gray100
            SUB_2 -> Color.Transparent
        }

    fun getContentColor(colors: PotiColors): Color {
        return when (this) {
            MAIN -> colors.white
            SUB_1 -> colors.gray900
            SUB_2 -> colors.poti600
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiModalButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiModalButton(
                text = "Main",
                onClick = {},
                modifier = Modifier.width(259.dp),
                type = ModalButtonType.MAIN,
            )

            PotiModalButton(
                text = "Sub-1",
                onClick = {},
                modifier = Modifier.width(259.dp),
                type = ModalButtonType.SUB_1,
            )

            PotiModalButton(
                text = "Sub-2",
                onClick = {},
                modifier = Modifier.width(259.dp),
                type = ModalButtonType.SUB_2,
            )
        }
    }
}
