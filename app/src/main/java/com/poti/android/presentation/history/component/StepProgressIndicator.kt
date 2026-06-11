package com.poti.android.presentation.history.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.PartyStatusType

private val stepLabels = listOf(
    R.string.history_step_progress_recruit_end,
    R.string.history_step_progress_payment_done,
    R.string.history_step_progress_shipping,
    R.string.history_step_progress_delivered,
)

/**
 * 단계별 프로그레스 인디케이터
 *
 * 파티 진행 상태를 4단계 라벨과 프로그레스 바로 시각화합니다.
 * Domain 레이어의 [PartyStatusType]을 직접 사용합니다.
 *
 * @param currentStatus 현재 파티 상태 (Domain 모델)
 * @param modifier Modifier
 */
@Composable
fun StepProgressIndicator(
    currentStatus: PartyStatusType,
    modifier: Modifier = Modifier,
) {
    val currentStep = currentStatus.toStepIndex()
    val colors = PotiTheme.colors

    val textMeasurer = rememberTextMeasurer()
    val textWidth = textMeasurer.measure(
        text = stringResource(R.string.history_step_progress_delivered),
        style = PotiTheme.typography.body14m,
    ).size.width.toFloat()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            stepLabels.forEachIndexed { index, label ->
                Text(
                    text = stringResource(label),
                    style = PotiTheme.typography.body14m,
                    color = getLabelColor(
                        labelIndex = index,
                        currentStep = currentStep,
                        activeColor = colors.poti600,
                        inactiveColor = colors.gray800,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
        ) {
            val circleRadius = 10.dp.toPx()
            val circleX = calculateCircleXPosition(
                currentStep = currentStep,
                totalWidth = size.width,
                circleRadius = circleRadius,
                textWidth = textWidth,
            )

            drawBackgroundBar(size.width, colors.gray300)

            if (currentStep > 1) {
                drawActiveProgressBar(circleX, circleRadius, colors.poti400)
            }

            if (currentStep > 0) {
                drawIndicatorCircle(circleX, circleRadius, colors.poti600)
            }
        }
    }
}

/**
 * PartyStatusType을 단계 인덱스(0-4)로 변환
 */
private fun PartyStatusType.toStepIndex(): Int = when (this) {
    PartyStatusType.RECRUITING -> 0
    PartyStatusType.CLOSED -> 1
    PartyStatusType.PAYMENT_DONE -> 2
    PartyStatusType.SHIPPING -> 3
    PartyStatusType.DELIVERED, PartyStatusType.COMPLETED -> 4
}

/**
 * 진행 단계에 따른 라벨 색상 결정
 */
private fun getLabelColor(
    labelIndex: Int,
    currentStep: Int,
    activeColor: Color,
    inactiveColor: Color,
): Color = if (labelIndex == currentStep - 1) activeColor else inactiveColor

/**
 * 현재 단계에 따른 인디케이터 원의 X 위치 계산
 */
private fun calculateCircleXPosition(
    currentStep: Int,
    textWidth: Float,
    totalWidth: Float,
    circleRadius: Float,
): Float = when (currentStep) {
    1 -> circleRadius
    2 -> (2 * totalWidth + textWidth) / 6
    3 -> (4 * totalWidth - textWidth) / 6
    4 -> totalWidth - circleRadius
    else -> 0f
}

/**
 * 배경 프로그레스 바 그리기 (회색, 비활성)
 */
private fun DrawScope.drawBackgroundBar(
    totalWidth: Float,
    color: Color,
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(x = 0f, y = 4.dp.toPx()),
        size = Size(width = totalWidth, height = 12.dp.toPx()),
        cornerRadius = CornerRadius(99.dp.toPx()),
    )
}

/**
 * 활성 프로그레스 바 그리기 (파란색, 원 중심까지 확장)
 */
private fun DrawScope.drawActiveProgressBar(
    circleX: Float,
    circleRadius: Float,
    color: Color,
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(x = 0f, y = 0f),
        size = Size(
            width = circleX + circleRadius / 2,
            height = 20.dp.toPx(),
        ),
        cornerRadius = CornerRadius(99.dp.toPx()),
    )
}

/**
 * 인디케이터 원 그리기 (현재 진행 위치)
 */
private fun DrawScope.drawIndicatorCircle(
    circleX: Float,
    circleRadius: Float,
    color: Color,
) {
    drawCircle(
        color = color,
        radius = circleRadius,
        center = Offset(x = circleX, y = 20.dp.toPx() / 2),
    )
}

private class StepProgressIndicatorPreviewProvider : PreviewParameterProvider<PartyStatusType> {
    override val values: Sequence<PartyStatusType>
        get() = PartyStatusType.entries.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun StepProgressIndicatorPreview(
    @PreviewParameter(StepProgressIndicatorPreviewProvider::class) currentStatus: PartyStatusType,
) {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = currentStatus,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
