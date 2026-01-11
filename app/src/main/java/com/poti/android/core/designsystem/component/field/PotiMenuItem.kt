package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiMenuItem(
    option: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    price: String? = null,
    disabled: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !disabled && isPressed -> PotiTheme.colors.gray100
        else -> PotiTheme.colors.white
    }

    val textColor = when {
        isSelected || disabled -> PotiTheme.colors.gray700
        else -> PotiTheme.colors.black
    }

    Row(
        modifier = modifier
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !disabled,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 15.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = option,
            color = textColor,
            style = PotiTheme.typography.body14m,
            modifier = Modifier.weight(1f),
        )

        if (price != null) {
            Text(
                text = price,
                color = textColor,
                style = PotiTheme.typography.body14m,
            )
        }
    }
}
