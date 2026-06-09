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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.PartyStatusType

private val stepLabels = listOf(
    "모집 완료",
    "입금 완료",
    "배송 시작",
    "배송 완료",
)

/**
 * 프로그레스 바 치수 상수 (Figma 디자인 기반)
 */
private object ProgressBarDimensions {
    val CIRCLE_RADIUS: Dp = 10.dp
    val BAR_HEIGHT_ACTIVE: Dp = 20.dp
    val BAR_HEIGHT_BACKGROUND: Dp = 12.dp
    val BACKGROUND_TOP_OFFSET: Dp = 4.dp
    val LABEL_BAR_SPACING: Dp = 12.dp
    val CORNER_RADIUS: Dp = 99.dp
}

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
    val labelPositions = remember { mutableStateListOf(0f, 0f, 0f, 0f) }
    val colors = PotiTheme.colors

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            stepLabels.forEachIndexed { index, label ->
                Text(
                    text = label,
                    style = PotiTheme.typography.body14m,
                    color = getLabelColor(
                        labelIndex = index,
                        currentStep = currentStep,
                        activeColor = colors.poti600,
                        inactiveColor = colors.gray800,
                    ),
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        labelPositions[index] = coordinates.calculateCenterX()
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(ProgressBarDimensions.LABEL_BAR_SPACING))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(ProgressBarDimensions.BAR_HEIGHT_ACTIVE),
        ) {
            val circleRadius = ProgressBarDimensions.CIRCLE_RADIUS.toPx()
            val circleX = calculateCircleXPosition(
                currentStep = currentStep,
                labelPositions = labelPositions,
                totalWidth = size.width,
                circleRadius = circleRadius,
            )

            drawBackgroundBar(size.width, colors.gray300)

            if (currentStep > 1 && circleX > 0f) {
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
 * 레이아웃의 중심 X 좌표 계산
 */
private fun LayoutCoordinates.calculateCenterX(): Float =
    positionInParent().x + size.width / 2f

/**
 * 진행 단계에 따른 라벨 색상 결정
 */
private fun getLabelColor(
    labelIndex: Int,
    currentStep: Int,
    activeColor: Color,
    inactiveColor: Color,
): Color = if (labelIndex < currentStep) activeColor else inactiveColor

/**
 * 현재 단계에 따른 인디케이터 원의 X 위치 계산
 */
private fun calculateCircleXPosition(
    currentStep: Int,
    labelPositions: List<Float>,
    totalWidth: Float,
    circleRadius: Float,
): Float = when (currentStep) {
    0 -> 0f
    1 -> circleRadius
    2 -> labelPositions.getOrElse(1) { 0f }
    3 -> labelPositions.getOrElse(2) { 0f }
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
        topLeft = Offset(x = 0f, y = ProgressBarDimensions.BACKGROUND_TOP_OFFSET.toPx()),
        size = Size(width = totalWidth, height = ProgressBarDimensions.BAR_HEIGHT_BACKGROUND.toPx()),
        cornerRadius = CornerRadius(ProgressBarDimensions.CORNER_RADIUS.toPx()),
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
            height = ProgressBarDimensions.BAR_HEIGHT_ACTIVE.toPx(),
        ),
        cornerRadius = CornerRadius(ProgressBarDimensions.CORNER_RADIUS.toPx()),
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
        center = Offset(x = circleX, y = ProgressBarDimensions.BAR_HEIGHT_ACTIVE.toPx() / 2),
    )
}

// ==================== Previews ====================

@Preview(showBackground = true, name = "Step 0 - Recruiting")
@Composable
private fun StepProgressIndicatorStep0Preview() {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = PartyStatusType.RECRUITING,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Step 1 - Closed")
@Composable
private fun StepProgressIndicatorStep1Preview() {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = PartyStatusType.CLOSED,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Step 2 - Payment Done")
@Composable
private fun StepProgressIndicatorStep2Preview() {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = PartyStatusType.PAYMENT_DONE,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Step 3 - Shipping")
@Composable
private fun StepProgressIndicatorStep3Preview() {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = PartyStatusType.SHIPPING,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Step 4 - COMPLETED")
@Composable
private fun StepProgressIndicatorStep4Preview() {
    PotiTheme {
        StepProgressIndicator(
            currentStatus = PartyStatusType.COMPLETED,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
