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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    val backgroundColor = when (type) {
        ModalButtonType.MAIN -> if (isPressed) PotiTheme.colors.poti800 else PotiTheme.colors.poti600
        ModalButtonType.SUB_1 -> if (isPressed) PotiTheme.colors.gray300 else PotiTheme.colors.gray100
        ModalButtonType.SUB_2 -> Color.Transparent
    }

    val contentColor = when (type) {
        ModalButtonType.MAIN -> PotiTheme.colors.white
        ModalButtonType.SUB_1 -> PotiTheme.colors.gray900
        ModalButtonType.SUB_2 -> PotiTheme.colors.poti600
    }

    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
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

            PotiSmallButton(
                text = "더보기",
                onClick = {},
            )


        }
    }
}
