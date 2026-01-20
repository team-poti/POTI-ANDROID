package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme

import com.poti.android.presentation.history.participant.model.DepositInfoUiModel
import com.poti.android.presentation.history.participant.model.DepositItemUiModel


@Composable
fun DepositInfoSection(
    info: DepositInfoUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(16.dp)),
    ) {
        Text(
            text = stringResource(id = R.string.history_deposit_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        PriceDetail(items = info.items, totalAmount = info.totalAmount)

        if (info.accountNumber != null && info.dueDate != null) {
            HistoryCalloutInfo(
                text = info.accountNumber,
                copyable = true,
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
            )

            HistoryCalloutInfo(
                text = info.dueDate,
                copyable = false,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            HistoryStateLabel(
                sizeType = StateLabelSize.LARGE,
                stageType = info.stage,
                statusType = info.status,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepositInfoSectionPreview() {
    PotiTheme {
        DepositInfoSection(
            info = DepositInfoUiModel(
                items = listOf(
                    DepositItemUiModel(
                        name = "해린 포토카드",
                        price = 15000,
                        type = PotiItemOptionType.MEMBER,
                    ),
                    DepositItemUiModel(
                        name = "GS25 반값택배",
                        price = 1800,
                        type = PotiItemOptionType.DELIVERY,
                    ),
                ),
                totalAmount = 16800,
                accountNumber = "카카오뱅크 3333-01-1234567",
                dueDate = "2024.12.31 23:59까지",
                stage = StateLabelStage.DEPOSIT,
                status = StateLabelStatus.WAIT,
            ),
        )
    }
}
