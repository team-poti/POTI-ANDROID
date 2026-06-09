package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun HistoryDetailContentHeader(
    text: String,
    modifier: Modifier = Modifier,
    onHeaderClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(vertical = 20.dp),
        )

        onHeaderClick?.let {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_lg),
                contentDescription = null,
                tint = PotiTheme.colors.gray700,
                modifier = Modifier
                    .padding(10.dp)
                    .size(24.dp)
                    .noRippleClickable(onHeaderClick),
            )
        }
    }
}

@Preview(showBackground = true, name = "Participants 0")
@Composable
private fun ParticipantManagementHeaderEmptyPreview() {
    PotiTheme {
        HistoryDetailContentHeader(
            text = "입금 정보",
            onHeaderClick = {},
        )
    }
}
