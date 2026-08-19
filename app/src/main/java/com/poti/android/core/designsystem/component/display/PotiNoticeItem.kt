package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

/**
 * Poti 알림 목록 아이템
 *
 * @param title 알림 제목
 * @param content 알림 본문
 * @param time 우측에 표시할 시간
 * @param isRead 읽음 여부, 읽지 않은 알림은 배경을 강조합니다
 * @param onClick 알림을 눌렀을 때 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 * @param contentPadding 콘텐츠에 적용할 여백
 */
@Composable
fun PotiNoticeItem(
    title: String,
    content: String,
    time: String,
    isRead: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    Row(
        modifier = modifier
            .heightIn(min = 81.dp)
            .background(if (isRead) colors.white else colors.gray100)
            .noRippleClickable(onClick)
            .padding(contentPadding),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = typography.body16sb,
                color = colors.black,
            )
            Text(
                text = content,
                style = typography.body14m,
                color = colors.gray800,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = time,
            style = typography.caption12m,
            color = colors.gray700,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PotiNoticeItemPreview() {
    PotiTheme {
        Column {
            PotiNoticeItem(
                title = "배송 시작",
                content = "아이브 메이크스타 거래건 배송이 시작되었어요",
                time = "3시간 전",
                isRead = true,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            PotiDivider(
                styleType = PotiDividerStyle.SMALL,
            )

            PotiNoticeItem(
                title = "배송 시작",
                content = "아이브 메이크스타 거래건 배송이 시작되었어요",
                time = "3시간 전",
                isRead = false,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
