package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.bottomBorder
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiMenuItem(
    option: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    price: String? = null,
    disabled: Boolean = false,
    showBottomBorder: Boolean = true,
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
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
            .heightIn(52.dp)
            .background(backgroundColor)
            .bottomBorder(
                strokeWidth = 1.dp,
                color = PotiTheme.colors.gray300,
                isVisible = showBottomBorder,
            )
            .noRippleClickable(
                onClick = onClick,
                enabled = !disabled,
                interactionSource = remember { MutableInteractionSource() },
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
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
                text = stringResource(R.string.party_option_price_won, price),
                color = textColor,
                style = PotiTheme.typography.body14m,
            )
        }
    }
}
