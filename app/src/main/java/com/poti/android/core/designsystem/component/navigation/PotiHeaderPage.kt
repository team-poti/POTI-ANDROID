package com.poti.android.core.designsystem.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.theme.PotiTheme

enum class PotiHeaderPageType(
    @DrawableRes val iconResId: Int,
) {
    BACK(R.drawable.ic_arrow_line_left),
    CLOSE(R.drawable.ic_x),
}

@Composable
fun PotiHeaderPage(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    potiHeaderPageType: PotiHeaderPageType = PotiHeaderPageType.BACK,
    title: String? = null,
    subTitle: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(PotiTheme.colors.white)
            .padding(horizontal = screenWidthDp(4.dp), vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PotiIconButton(
            iconRes = potiHeaderPageType.iconResId,
            onClick = onNavigationClick,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            title?.let {
                Text(
                    text = title,
                    style = PotiTheme.typography.body16sb,
                    color = PotiTheme.colors.black,
                )
            }

            subTitle?.let {
                Text(
                    text = subTitle,
                    style = PotiTheme.typography.caption10m.copy(
                        lineHeight = 1.35.em,
                    ),
                    color = PotiTheme.colors.gray700,
                )
            }
        }

        onTrailingIconClick?.let {
            PotiIconButton(
                iconRes = R.drawable.ic_switch,
                onClick = onTrailingIconClick,
            )
        }
    }
}

@Preview
@Composable
private fun PotiHeaderPagePreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PotiHeaderPage(
                onNavigationClick = {},
                potiHeaderPageType = PotiHeaderPageType.CLOSE,
            )

            PotiHeaderPage(
                onNavigationClick = {},
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
                subTitle = "서브 타이틀",
            )

            PotiHeaderPage(
                onNavigationClick = {},
                title = "헤더 타이틀",
                subTitle = "서브 타이틀",
                onTrailingIconClick = {},
            )
        }
    }
}
