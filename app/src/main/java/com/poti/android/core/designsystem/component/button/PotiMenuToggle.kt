package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

/**
 * Poti 설정 화면용 제목 + 설명 토글 행
 *
 * @param title 좌측 상단 제목
 * @param description 제목 아래 설명
 * @param checked 토글 on/off 여부
 * @param onCheckedChange 토글을 눌렀을 때 변경될 상태와 함께 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 * @param contentPadding 콘텐츠에 적용할 여백
 */
@Composable
fun PotiMenuToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 15.dp, bottom = 16.dp),
) {
    Row(
        modifier = modifier
            .heightIn(min = 80.dp)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
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
                text = description,
                style = typography.body14m,
                color = colors.gray800,
            )
        }

        PotiToggle(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun PotiMenuTogglePreview() {
    var checked by remember { mutableStateOf(true) }

    PotiTheme {
        Column {
            PotiMenuToggle(
                title = "거래 알림",
                description = "분철 모집/참여 거래 알림",
                checked = true,
                onCheckedChange = {},
                modifier = Modifier.fillMaxWidth(),
            )

            PotiMenuToggle(
                title = "거래 알림",
                description = "분철 모집/참여 거래 알림",
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.fillMaxWidth(),
            )

            PotiMenuToggle(
                title = "거래 알림",
                description = "분철 모집/참여 거래 알림",
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
