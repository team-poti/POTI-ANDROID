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
fun PotiChipButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ChipButtonType = ChipButtonType.DEFAULT,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when (type) {
        ChipButtonType.DEFAULT -> if (isPressed) PotiTheme.colors.gray300 else PotiTheme.colors.gray100
        ChipButtonType.SELECTED -> if (isPressed) PotiTheme.colors.poti800 else PotiTheme.colors.poti600
    }

    val contentColor = when (type) {
        ChipButtonType.DEFAULT -> PotiTheme.colors.gray800
        ChipButtonType.SELECTED -> PotiTheme.colors.white
    }

    Row(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
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

enum class ChipButtonType {
    DEFAULT,
    SELECTED,
}

@Preview(showBackground = true)
@Composable
private fun PotiChipButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiChipButton(
                text = "Default",
                onClick = {},
                modifier = Modifier.width(165.dp),
                type = ChipButtonType.DEFAULT,
            )

            PotiChipButton(
                text = "Selected",
                onClick = {},
                modifier = Modifier.width(165.dp),
                type = ChipButtonType.SELECTED,
            )
        }
    }
}
