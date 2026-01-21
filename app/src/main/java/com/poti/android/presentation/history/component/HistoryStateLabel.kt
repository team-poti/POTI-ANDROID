package com.poti.android.presentation.history.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

enum class StateLabelSize {
    LARGE,
    SMALL,
}

enum class StateLabelStage(
    @StringRes val text: Int,
) {
    DEPOSIT(R.string.history_participant_stage_deposit),
    DELIVERY(R.string.history_participant_stage_delivery),
    RECRUIT(R.string.history_participant_stage_recruit),
}

enum class StateLabelStatus(
    @StringRes val text: Int,
) {
    WAIT(R.string.history_participant_stage_wait),
    ING(R.string.history_participant_stage_ing),
    CHECK(R.string.history_participant_stage_check),
    START(R.string.history_participant_stage_start),
    DONE(R.string.history_participant_stage_done),
}

@Composable
fun HistoryStateLabel(
    sizeType: StateLabelSize,
    stageType: StateLabelStage,
    statusType: StateLabelStatus,
    modifier: Modifier = Modifier,
) {
    val (text, style, color) =
        getTextInfo(sizeType, stageType, statusType)

    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier,
    )
}

@Composable
private fun getTextInfo(
    sizeType: StateLabelSize,
    stageType: StateLabelStage,
    statusType: StateLabelStatus,
): Triple<String, TextStyle, Color> {
    val text = stringResource(
        id = R.string.history_participant_stage_and_status_format,
        stringResource(stageType.text),
        stringResource(statusType.text),
    )

    val style = when (sizeType) {
        StateLabelSize.LARGE -> PotiTheme.typography.body16sb
        StateLabelSize.SMALL -> PotiTheme.typography.body14sb
    }

    val color = getStateColor(stageType, statusType, sizeType)

    return Triple(text, style, color)
}

@Composable
private fun getStateColor(
    stage: StateLabelStage,
    status: StateLabelStatus,
    size: StateLabelSize,
): Color {
    val isLarge = size == StateLabelSize.LARGE
    val defaultColor = colors.gray700

    return when (stage to status) {
        // DEPOSIT 단계
        StateLabelStage.DEPOSIT to StateLabelStatus.WAIT ->
            if (isLarge) colors.sementicRed else defaultColor

        StateLabelStage.DEPOSIT to StateLabelStatus.CHECK ->
            if (isLarge) colors.poti600 else colors.sementicRed

        StateLabelStage.DEPOSIT to StateLabelStatus.DONE ->
            if (isLarge) defaultColor else colors.poti600

        // DELIVERY 단계
        StateLabelStage.DELIVERY to StateLabelStatus.WAIT ->
            if (isLarge) colors.sementicRed else defaultColor

        StateLabelStage.DELIVERY to StateLabelStatus.START -> colors.poti600
        StateLabelStage.DELIVERY to StateLabelStatus.DONE -> defaultColor

        // RECRUIT 단계
        StateLabelStage.RECRUIT to StateLabelStatus.ING -> colors.sementicRed
        StateLabelStage.RECRUIT to StateLabelStatus.DONE -> colors.poti600

        else -> defaultColor
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryStateLabelPreview() {
    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HistoryStateLabel(
                sizeType = StateLabelSize.SMALL,
                stageType = StateLabelStage.DEPOSIT,
                statusType = StateLabelStatus.WAIT,
            )
            HistoryStateLabel(
                sizeType = StateLabelSize.SMALL,
                stageType = StateLabelStage.DEPOSIT,
                statusType = StateLabelStatus.CHECK,
            )
            HistoryStateLabel(
                sizeType = StateLabelSize.LARGE,
                stageType = StateLabelStage.DEPOSIT,
                statusType = StateLabelStatus.CHECK,
            )
            HistoryStateLabel(
                sizeType = StateLabelSize.LARGE,
                stageType = StateLabelStage.DEPOSIT,
                statusType = StateLabelStatus.WAIT,
            )
            HistoryStateLabel(
                sizeType = StateLabelSize.LARGE,
                stageType = StateLabelStage.DELIVERY,
                statusType = StateLabelStatus.WAIT,
            )
            HistoryStateLabel(
                sizeType = StateLabelSize.LARGE,
                stageType = StateLabelStage.RECRUIT,
                statusType = StateLabelStatus.ING,
            )
        }
    }
}
