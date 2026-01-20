package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun ParticipantGuidelines(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.party_detail_announcement),
        style = PotiTheme.typography.caption12m,
        color = PotiTheme.colors.gray800,
        modifier = modifier
            .padding(horizontal = screenWidthDp(16.dp), vertical = 16.dp)
            .padding(bottom = 40.dp),
    )
}
