package com.poti.android.presentation.history.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ProgressInfo

@Composable
fun ProgressStatusSection(
    progressInfo: ProgressInfo,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.history_progress_status_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier
                .padding(bottom = 20.dp),
        )
        HistoryStateGuide(
            text = progressInfo.guideText,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        Icon(
            imageVector = ImageVector.vectorResource(
                getStepIndicatorDrawable(progressInfo.step),
            ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(horizontal = screenWidthDp(8.dp))
                .align(Alignment.CenterHorizontally),
        )
    }
}

@DrawableRes
fun getStepIndicatorDrawable(step: Int): Int {
    return when (step) {
        0 -> R.drawable.ic_history_step_indicator_0
        1 -> R.drawable.ic_history_step_indicator_1
        2 -> R.drawable.ic_history_step_indicator_2
        3 -> R.drawable.ic_history_step_indicator_3
        4 -> R.drawable.ic_history_step_indicator_4
        else -> R.drawable.ic_history_step_indicator_0
    }
}

@Preview(showBackground = true, name = "Step 0 (Recruit)")
@Composable
private fun ProgressStatusSectionStep0Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = ProgressInfo(
                guideText = "모집이 시작되었습니다.",
                step = 0,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Step 1 (Deposit)")
@Composable
private fun ProgressStatusSectionStep1Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = ProgressInfo(
                guideText = "입금을 확인하고 있습니다.",
                step = 1,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Step 2 (Delivery)")
@Composable
private fun ProgressStatusSectionStep2Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = ProgressInfo(
                guideText = "배송이 시작되었습니다.",
                step = 2,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Step 4 (Complete)")
@Composable
private fun ProgressStatusSectionStep4Preview() {
    PotiTheme {
        ProgressStatusSection(
            progressInfo = ProgressInfo(
                guideText = "거래가 완료되었습니다.",
                step = 4,
            ),
        )
    }
}
