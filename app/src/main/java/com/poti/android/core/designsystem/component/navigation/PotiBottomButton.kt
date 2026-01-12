package com.poti.android.core.designsystem.component.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subText: String? = null,
    onSubClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(PotiTheme.colors.white)
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (subText != null && onSubClick != null) {
            PotiActionButton(
                text = subText,
                onClick = onSubClick,
                type = ActionButtonType.PRIMARY_SUB,
                modifier = Modifier.weight(119f),
            )
        }

        PotiActionButton(
            text = text,
            onClick = onClick,
            type = ActionButtonType.PRIMARY_MAIN,
            modifier = Modifier.weight(216f),
        )
    }
}

@Preview
@Composable
private fun PotiBottomButtonPreview() {
    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            PotiBottomButton(
                text = "버튼",
                onClick = {},
            )

            PotiBottomButton(
                text = "버튼",
                onClick = {},
                subText = "버튼",
                onSubClick = {},
            )
        }
    }
}
