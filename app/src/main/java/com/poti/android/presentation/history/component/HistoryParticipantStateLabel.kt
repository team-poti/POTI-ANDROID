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

enum class ParticipantStateLabelSize {
    LARGE,
    SMALL,
}

enum class ParticipantStateLabelStage(
    @StringRes val text: Int,
) {
    DEPOSIT(R.string.history_participant_stage_deposit),
    DELIVERY(R.string.history_participant_stage_delivery),
    RECRUIT(R.string.history_participant_stage_recruit),
}

enum class ParticipantStateLabelStatus(
    @StringRes val text: Int,
) {
    WAIT(R.string.history_participant_stage_wait),
    CHECK(R.string.history_participant_stage_check),
    START(R.string.history_participant_stage_start),
    DONE(R.string.history_participant_stage_done),
}

@Composable
fun HistoryParticipantStateLabel(
    sizeType: ParticipantStateLabelSize,
    stageType: ParticipantStateLabelStage,
    statusType: ParticipantStateLabelStatus,
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
    sizeType: ParticipantStateLabelSize,
    stageType: ParticipantStateLabelStage,
    statusType: ParticipantStateLabelStatus,
): Triple<String, TextStyle, Color> {
    val text = stringResource(
        id = R.string.history_participant_stage_and_status_format,
        stringResource(stageType.text),
        stringResource(statusType.text),
    )

    val style = when (sizeType) {
        ParticipantStateLabelSize.LARGE -> PotiTheme.typography.body16sb
        ParticipantStateLabelSize.SMALL -> PotiTheme.typography.body14sb
    }

    val color = getStateColor(stageType, statusType, sizeType)

    return Triple(text, style, color)
}

@Composable
private fun getStateColor(
    stage: ParticipantStateLabelStage,
    status: ParticipantStateLabelStatus,
    size: ParticipantStateLabelSize,
): Color {
    val isLarge = size == ParticipantStateLabelSize.LARGE
    val defaultColor = colors.gray700

    return when (stage to status) {
        // DEPOSIT 단계
        ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.WAIT ->
            if (isLarge) colors.sementicRed else defaultColor

        ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.CHECK ->
            if (isLarge) colors.poti600 else colors.sementicRed

        ParticipantStateLabelStage.DEPOSIT to ParticipantStateLabelStatus.DONE ->
            if (isLarge) defaultColor else colors.poti600

        // DELIVERY 단계
        ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.WAIT ->
            if (isLarge) colors.sementicRed else defaultColor

        ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.START -> colors.poti600
        ParticipantStateLabelStage.DELIVERY to ParticipantStateLabelStatus.DONE -> defaultColor

        // RECRUIT 단계
        ParticipantStateLabelStage.RECRUIT to ParticipantStateLabelStatus.WAIT -> colors.sementicRed
        ParticipantStateLabelStage.RECRUIT to ParticipantStateLabelStatus.DONE -> colors.poti600

        else -> defaultColor
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantStateLabelPreview() {
    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.SMALL,
                stageType = ParticipantStateLabelStage.DEPOSIT,
                statusType = ParticipantStateLabelStatus.WAIT,
            )
            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.SMALL,
                stageType = ParticipantStateLabelStage.DEPOSIT,
                statusType = ParticipantStateLabelStatus.CHECK,
            )
            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.LARGE,
                stageType = ParticipantStateLabelStage.DEPOSIT,
                statusType = ParticipantStateLabelStatus.CHECK,
            )
            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.LARGE,
                stageType = ParticipantStateLabelStage.DEPOSIT,
                statusType = ParticipantStateLabelStatus.WAIT,
            )
            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.LARGE,
                stageType = ParticipantStateLabelStage.DELIVERY,
                statusType = ParticipantStateLabelStatus.WAIT,
            )
        }
    }
}
