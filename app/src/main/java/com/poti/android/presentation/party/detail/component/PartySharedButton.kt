package com.poti.android.presentation.party.detail.component

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PartySharedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_share),
            contentDescription = null,
            tint = PotiTheme.colors.gray800,
            modifier = Modifier.size(24.dp),
        )

        Text(
            text = stringResource(R.string.party_detail_share_button),
            color = PotiTheme.colors.gray800,
            style = PotiTheme.typography.button14sb,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PartySharedButtonPreview() {
    PotiTheme {
        PartySharedButton(
            onClick = { },
        )
    }
}
