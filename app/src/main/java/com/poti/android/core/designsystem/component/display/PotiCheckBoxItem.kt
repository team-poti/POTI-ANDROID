package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

/**
 * Poti 체크박스 + 라벨 UI
 *
 * @param text 체크박스 우측에 표시할 라벨
 * @param selected 체크박스 선택 여부
 * @param onCheckedChange 항목을 눌렀을 때 변경될 상태와 함께 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 * @param enabled 활성화 여부, 비활성 시 흐리게 표시되며 클릭되지 않습니다
 */
@Composable
fun PotiCheckBoxItem(
    text: String,
    selected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.4f)
            .selectable(
                selected = selected,
                enabled = enabled,
                interactionSource = null,
                indication = null,
                onClick = { onCheckedChange(!selected) },
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PotiCheckBox(selected = selected)
        Text(
            text = text,
            style = typography.body14m,
            color = colors.gray900,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiCheckBoxItemPreview() {
    var selected by remember { mutableStateOf(false) }

    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiCheckBoxItem(text = "버튼", selected = false, onCheckedChange = {})

            PotiCheckBoxItem(text = "버튼", selected = true, onCheckedChange = {})

            PotiCheckBoxItem(text = "버튼", selected = false, onCheckedChange = {}, enabled = false)

            PotiCheckBoxItem(text = "버튼", selected = true, onCheckedChange = {}, enabled = false)

            PotiCheckBoxItem(text = "버튼", selected = selected, onCheckedChange = { selected = it })
        }
    }
}
