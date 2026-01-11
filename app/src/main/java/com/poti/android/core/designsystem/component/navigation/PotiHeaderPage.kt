package com.poti.android.core.designsystem.component.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiHeaderPage(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(PotiTheme.colors.white)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PotiIconButton(
            iconRes = if (title.isNullOrBlank()) R.drawable.ic_arrow_line_left else R.drawable.ic_x,
            onClick = onNavigationClick,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
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
                    style = PotiTheme.typography.caption10m,
                    color = Color(0xFFB3B3B3), // TODO: [지현] 컬러 시스템 수정
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
