package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.party.PartyDetail

@Composable
fun PartyDetailContent(
    partyDetail: PartyDetail,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val textStyle = PotiTheme.typography.body14m

    val textHeightDp = with(density) {
        if (textStyle.lineHeight.isSp) {
            textStyle.lineHeight.toDp()
        } else {
            textStyle.fontSize.toDp()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = partyDetail.content,
            style = PotiTheme.typography.body16m,
            color = PotiTheme.colors.black,
        )

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.widthIn(min = 76.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.party_recruit_deadline),
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800,
                )
                Text(
                    text = stringResource(R.string.party_detail_shipping_price),
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.party_detail_deadline, partyDetail.deadline),
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.black,
                )

                FlowRow(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    partyDetail.deliveryOptions.forEachIndexed { index, option ->
                        Text(
                            text = stringResource(R.string.party_detail_shipping_price_format, option.name, option.price.toMoneyString()),
                            style = PotiTheme.typography.body14m,
                            color = PotiTheme.colors.black,
                        )

                        if (index < partyDetail.deliveryOptions.lastIndex) {
                            VerticalDivider(
                                thickness = 1.dp,
                                color = PotiTheme.colors.gray800,
                                modifier = Modifier.height(textHeightDp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyDetailContentPreview() {
    PotiTheme {
        PartyDetailContent(
            partyDetail = UiMockData.partyDetail,
        )
    }
}
