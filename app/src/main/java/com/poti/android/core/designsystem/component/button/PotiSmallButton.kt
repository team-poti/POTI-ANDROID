package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .noRippleClickable { onClick() }
            .padding(start = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body14m,
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_down_sm),
            contentDescription = null,
            tint = PotiTheme.colors.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiSmallButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiSmallButton(
                text = "더보기",
                onClick = {},
            )
        }
    }
}
