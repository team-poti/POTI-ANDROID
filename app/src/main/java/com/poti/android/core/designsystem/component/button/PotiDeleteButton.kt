package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiColors
import com.poti.android.core.designsystem.theme.PotiTheme

enum class DeleteButtonType {
    DARK,
    LIGHT,
    ;

    fun getBackgroundColor(colors: PotiColors): Color {
        return when (this) {
            DARK -> colors.gray300
            LIGHT -> colors.white
        }
    }
}

@Composable
fun PotiDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: DeleteButtonType = DeleteButtonType.DARK,
) {
    val colors = PotiTheme.colors
    val backgroundColor = type.getBackgroundColor(colors)

    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.icn_x_sm),
        contentDescription = null,
        modifier = modifier
            .noRippleClickable(onClick)
            .padding(13.dp)
            .size(24.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        tint = PotiTheme.colors.gray700,
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiDeleteButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiDeleteButton(
                type = DeleteButtonType.DARK,
                onClick = {},
            )

            PotiDeleteButton(
                type = DeleteButtonType.LIGHT,
                onClick = {},
            )
        }
    }
}
