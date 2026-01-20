package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun ParticipantManagementHeader(
    participantCount: Int,
    onHeaderClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = screenWidthDp(16.dp),
                end = screenWidthDp(4.dp),
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.history_participant_management_title, participantCount),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_lg),
            contentDescription = null,
            tint = PotiTheme.colors.gray700,
            modifier = Modifier.noRippleClickable(onHeaderClick)
        )
    }
}

@Preview(showBackground = true, name = "Participants 0")
@Composable
private fun ParticipantManagementHeaderEmptyPreview() {
    PotiTheme {
        ParticipantManagementHeader(
            participantCount = 0,
            onHeaderClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Participants 5")
@Composable
private fun ParticipantManagementHeaderMultiPreview() {
    PotiTheme {
        ParticipantManagementHeader(
            participantCount = 5,
            onHeaderClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Participants 99+")
@Composable
private fun ParticipantManagementHeaderManyPreview() {
    PotiTheme {
        ParticipantManagementHeader(
            participantCount = 100,
            onHeaderClick = {},
        )
    }
}
