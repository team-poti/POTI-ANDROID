package com.poti.android.core.designsystem.component.modal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.button.ModalButtonType
import com.poti.android.core.designsystem.component.button.PotiModalButton
import com.poti.android.core.designsystem.theme.PotiTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PotiNoticeModal(
    title: String,
    subtitle: String,
    notices: ImmutableList<String>,
    agreement: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PotiModal(
        onDismissRequest = onDismiss,
        modifier = modifier.padding(horizontal = 28.dp),
    ) {
        PotiNoticeModalContent(
            title = title,
            subtitle = subtitle,
            notices = notices,
            agreement = agreement,
            confirmButtonText = confirmButtonText,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}

@Composable
private fun PotiNoticeModalContent(
    title: String,
    subtitle: String,
    notices: ImmutableList<String>,
    agreement: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NoticeModalHeader(
            title = title,
            onDismiss = onDismiss,
        )

        Spacer(Modifier.height(5.dp))

        NoticeTextBox(
            text = subtitle,
            color = PotiTheme.colors.poti800,
        )

        Spacer(Modifier.height(12.dp))

        NoticeList(notices = notices)

        Spacer(Modifier.height(12.dp))

        NoticeTextBox(
            text = agreement,
            color = PotiTheme.colors.gray900,
        )

        Spacer(Modifier.height(12.dp))

        PotiModalButton(
            text = confirmButtonText,
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            type = ModalButtonType.SECONDARY,
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun NoticeModalHeader(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp, top = 4.dp),
    ) {
        Text(
            text = title,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.title18sb,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_x),
            contentDescription = null,
            tint = PotiTheme.colors.black,
            modifier = Modifier
                .noRippleClickable(onClick = onDismiss)
                .padding(all = 12.dp),
        )
    }
}

@Composable
private fun NoticeTextBox(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = color,
        style = PotiTheme.typography.body14m,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(
                color = PotiTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(vertical = 8.dp),
    )
}

@Composable
private fun NoticeList(
    notices: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val showBottomGradient by remember { derivedStateOf { scrollState.canScrollForward } }

    Box(modifier = modifier.heightIn(max = 280.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
        ) {
            notices.forEach { notice ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_bullet),
                        contentDescription = null,
                        tint = PotiTheme.colors.gray900,
                    )

                    Text(
                        text = notice,
                        color = PotiTheme.colors.gray900,
                        style = PotiTheme.typography.body14m,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        if (showBottomGradient) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, PotiTheme.colors.white),
                        ),
                    ),
            )
        }

        if (scrollState.maxValue > 0) {
            val scrollbarColor = PotiTheme.colors.gray300

            Canvas(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 2.dp)
                    .fillMaxHeight()
                    .width(3.dp),
            ) {
                val maxScroll = scrollState.maxValue.toFloat()
                val totalContentHeight = size.height + maxScroll
                val thumbHeight = (size.height * size.height / totalContentHeight)
                    .coerceAtLeast(24.dp.toPx())
                    .coerceAtMost(size.height)
                val thumbOffsetY = (scrollState.value / maxScroll) * (size.height - thumbHeight)

                drawRoundRect(
                    color = scrollbarColor,
                    topLeft = Offset(0f, thumbOffsetY),
                    size = Size(size.width, thumbHeight),
                    cornerRadius = CornerRadius(size.width / 2),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PotiNoticeModalContentPreview() {
    PotiTheme {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PotiTheme.colors.white,
        ) {
            PotiNoticeModalContent(
                title = "모집자 안내 사항",
                subtitle = "모집 전 꼭 확인해 주세요!",
                notices = persistentListOf(
                    "모집 시작 후에는 모집 정보를 수정할 수 없습니다.",
                    "참여자가 있으면 모집글을 삭제할 수 없습니다.",
                ),
                agreement = "위 내용을 확인하였으며,\n안내 사항을 준수하겠습니다.",
                confirmButtonText = "확인",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
