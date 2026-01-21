package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun TotalPrice(
    totalPrice: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.party_join_total_price),
            style = PotiTheme.typography.body16m,
            color = PotiTheme.colors.black,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.party_option_price_won, totalPrice),
            style = PotiTheme.typography.display20b,
            color = PotiTheme.colors.black,
        )
    }
}
