package com.poti.android.core.designsystem.component.button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
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
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

/**
 * Poti 토글 스위치 UI
 *
 * @param checked 토글 on/off 여부
 * @param onCheckedChange 토글을 눌렀을 때 변경될 상태와 함께 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 */
@Composable
fun PotiToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackColor = if (checked) colors.poti600 else colors.gray500
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        label = "thumbOffset",
    )

    Box(
        modifier = modifier
            .width(46.dp)
            .height(26.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(trackColor)
            .selectable(
                selected = checked,
                interactionSource = null,
                indication = null,
                onClick = { onCheckedChange(!checked) },
            )
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(colors.white),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiTogglePreview() {
    var checked by remember { mutableStateOf(true) }

    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiToggle(checked = true, onCheckedChange = {})

            PotiToggle(checked = false, onCheckedChange = {})

            PotiToggle(checked = checked, onCheckedChange = { checked = it })
        }
    }
}
