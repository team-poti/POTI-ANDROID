package com.poti.android.core.designsystem.component.field

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
internal fun FieldLabel(
    label: String,
) {
    if (label.isNotEmpty()) {
        Text(
            text = label,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body14sb,
        )
    }
}
