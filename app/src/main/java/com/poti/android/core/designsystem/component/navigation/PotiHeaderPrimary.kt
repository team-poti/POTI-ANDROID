package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiHeaderPrimary(
    @DrawableRes firstIconRes: Int,
    onFirstIconClick: () -> Unit,
    @DrawableRes secondIconRes: Int,
    onSecondIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    Row(
        modifier = modifier
            .background(PotiTheme.colors.white)
            .padding(start = 20.dp, end = 4.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = PotiTheme.typography.title18sb,
                color = PotiTheme.colors.black,
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PotiIconButton(
            iconRes = firstIconRes,
            onClick = onFirstIconClick,
            tint = PotiTheme.colors.black,
        )

        PotiIconButton(
            iconRes = secondIconRes,
            onClick = onSecondIconClick,
            tint = PotiTheme.colors.black,
        )
    }
}

@Preview
@Composable
private fun PotiHeaderPrimaryPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiHeaderPrimary(
                firstIconRes = R.drawable.ic_search,
                onFirstIconClick = {},
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = {},
            )
            PotiHeaderPrimary(
                title = "마이",
                firstIconRes = R.drawable.ic_setting,
                onFirstIconClick = {},
                secondIconRes = R.drawable.ic_alarm,
                onSecondIconClick = {},
            )
        }
    }
}
