package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poti.android.core.designsystem.theme.PotiTheme

// TODO: [도연] Display>ErrorMessage 병합 시 삭제
@Composable
fun FieldErrorMessage(
    error: String,
    modifier: Modifier = Modifier,
) {
    if (error.isNotEmpty()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = error,
                color = PotiTheme.colors.sementicRed,
                style = PotiTheme.typography.body14m,
            )
        }
    }
}
