package com.poti.android.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .heightIn(min = 48.dp)
            .noRippleClickable(onClick)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(10.dp),
        color = PotiTheme.colors.gray800,
        style = PotiTheme.typography.body14m,
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiTextButtonPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PotiTextButton(
                text = "더보기",
                onClick = {},
            )
        }
    }
}
