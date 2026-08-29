package com.poti.android.presentation.alarm.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun AlarmReadButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.alarm_read_all),
        color = if (enabled) PotiTheme.colors.white else PotiTheme.colors.gray700,
        style = PotiTheme.typography.button16sb,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(
                onClick = onClick,
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(
                color = if (enabled) PotiTheme.colors.black else PotiTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(all = 14.dp)
    )
}

private class AlarmReadButtonPreviewProvider: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview(showBackground = true)
@Composable
private fun AlarmReadButtonPreview(
    @PreviewParameter(AlarmReadButtonPreviewProvider::class) enabled: Boolean,
) {
    PotiTheme {
        AlarmReadButton(
            onClick = {},
            enabled = enabled,
            modifier = Modifier.padding(all = 20.dp)
        )
    }
}
