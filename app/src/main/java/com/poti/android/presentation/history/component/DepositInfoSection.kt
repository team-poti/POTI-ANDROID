package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.DepositStatus
import com.poti.android.domain.model.history.ParticipantDepositInfo
import com.poti.android.presentation.history.mapper.toUiState

@Composable
fun DepositInfoSection(
    info: ParticipantDepositInfo,
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

        val depositStatus = info.depositStatus
        if (depositStatus is DepositStatus.DepositCheck ||
            depositStatus is DepositStatus.DepositWait
        ) {
            HistoryCalloutInfo(
                text = depositStatus.accountNumber,
                copyable = true,
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
            )

            HistoryCalloutInfo(
                text = depositStatus.dueDate,
                copyable = false,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            val (stage, status) = depositStatus.toUiState()

            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.LARGE,
                stageType = stage,
                statusType = status,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}
