package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

/**
 * Poti 원형 아이콘 버튼
 *
 * @param onClick 버튼을 눌렀을 때 호출되는 콜백
 * @param modifier 컴포넌트에 적용할 modifier
 */
@Composable
fun PotiCircleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colors.black)
            .noRippleClickable(onClick)
            .padding(6.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiCircleIconButtonPreview() {
    PotiTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PotiCircleIconButton(onClick = {})
        }
    }
}
