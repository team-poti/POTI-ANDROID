package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun BadgeButton(
    bias: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .heightIn(min = 40.dp)
            .clip(CircleShape)
            .background(PotiTheme.colors.poti400)
            .noRippleClickable(onClick)
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = bias,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body14m,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_sm),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = PotiTheme.colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BadgeButton(
                bias = "나의 최애 선택",
                onClick = {},
                modifier = Modifier,
            )

            BadgeButton(
                bias = "아이브",
                onClick = {},
                modifier = Modifier,
            )
        }
    }
}
