package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.PartyStatusType

@Composable
fun ProgressStatusSection(
    progressStatus: PartyStatusType,
    statusMessage: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.history_progress_status_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(vertical = 20.dp),
        )
        HistoryStateGuide(
            text = statusMessage,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        StepProgressIndicator(
            currentStatus = progressStatus,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(8.dp))
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Preview(showBackground = true, name = "Step 0 (Recruit)")
@Composable
private fun ProgressStatusSectionStep0Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressStatus = PartyStatusType.RECRUITING,
            statusMessage = "참여자를 기다리고 있어요",
        )
    }
}

@Preview(showBackground = true, name = "Step 1 (Deposit)")
@Composable
private fun ProgressStatusSectionStep1Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressStatus = PartyStatusType.CLOSED,
            statusMessage = "입금을 기다리는 중이에요",
        )
    }
}

@Preview(showBackground = true, name = "Step 2 (Delivery)")
@Composable
private fun ProgressStatusSectionStep2Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressStatus = PartyStatusType.PAYMENT_DONE,
            statusMessage = "배송을 기다리는 참여자가 있어요",
        )
    }
}

@Preview(showBackground = true, name = "Step 3 (Shipping)")
@Composable
private fun ProgressStatusSectionStep3Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressStatus = PartyStatusType.SHIPPING,
            statusMessage = "배송을 기다리는 참여자가 있어요",
        )
    }
}

@Preview(showBackground = true, name = "Step 4 (Complete)")
@Composable
private fun ProgressStatusSectionStep4Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressStatus = PartyStatusType.DELIVERED,
            statusMessage = "거래가 종료되었어요",
        )
    }
}
