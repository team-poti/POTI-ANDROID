package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * Poti 체크박스 UI
 *
 * @param selected 체크박스 선택 여부
 * @param modifier 컴포넌트에 적용할 modifier
 * @param onClick 체크여부만을 표현하기 위해서 null, 혹은 설정하지 않을 수 있습니다
 */
@Composable
fun PotiCheckBox(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val trackColor = if (selected) PotiTheme.colors.poti200 else PotiTheme.colors.gray300
    val thumbColor = if (selected) PotiTheme.colors.poti600 else PotiTheme.colors.gray700

    Box(
        modifier = modifier
            .size(24.dp)
            .padding(2.dp)
            .run {
                if (onClick != null) {
                    noRippleClickable(onClick = onClick)
                } else {
                    this
                }
            }
            .clip(RoundedCornerShape(8.dp))
            .background(trackColor),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(thumbColor),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiCheckBoxPreview() {
    var selected by remember { mutableStateOf(false) }

    PotiTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PotiCheckBox(selected = selected, onClick = { selected = !selected })
        }
    }
}
