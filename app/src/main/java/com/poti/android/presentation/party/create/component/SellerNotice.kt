package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun SellerNotice(
    modifier: Modifier = Modifier,
) {
    val texts = listOf(
        stringResource(R.string.create_notice_title),
        stringResource(R.string.create_notice_first),
        stringResource(R.string.create_notice_second),
        stringResource(R.string.create_notice_third),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = screenWidthDp(16.dp),
                vertical = 24.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        texts.forEach { text ->
            Text(
                text = text,
                color = PotiTheme.colors.gray800,
                style = PotiTheme.typography.caption12m,
            )
        }
    }
}

@Preview
@Composable
private fun SellerNoticePreveiw() {
    SellerNotice()
}
