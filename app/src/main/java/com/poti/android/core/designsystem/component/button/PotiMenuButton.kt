package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

/**
 * Poti 설정 화면용 리스트 행 버튼
 *
 * 배경과 클릭 영역이 컴포넌트 전체를 차지하므로, 눌린 배경을 좌우 끝까지 채우려면
 * 호출부에서 `Modifier.fillMaxWidth()`를 넘깁니다.
 *
 * @param text 좌측에 표시할 라벨
 * @param onClick 항목을 눌렀을 때 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 * @param trailingText 우측에 표시할 보조 텍스트, null이면 화살표 아이콘을 표시합니다
 * @param contentPadding 배경 안쪽 콘텐츠에 적용할 여백
 */
@Composable
fun PotiMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isPressed) colors.gray100 else Color.Transparent)
            .noRippleClickable(
                interactionSource = interactionSource,
                onClick = onClick,
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = typography.body16sb,
            color = colors.black,
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = typography.body16sb,
                color = colors.gray800,
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_lg),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colors.gray700,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PotiMenuButtonPreview() {
    PotiTheme {
        Column {
            PotiMenuButton(
                text = "내 계정",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            PotiMenuButton(
                text = "내 프로필 관리",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            PotiMenuButton(
                text = "버전 정보",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                trailingText = "1.0.0",
            )
        }
    }
}
