package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiIconButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = PotiTheme.colors.black,
) {
    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = null,
        modifier = modifier
            .size(48.dp)
            .noRippleClickable(onClick)
            .padding(12.dp),
        tint = tint,
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiIconButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiIconButton(
                iconRes = R.drawable.ic_heart,
                onClick = {},
            )
        }
    }
}
