package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_notice),
            contentDescription = null,
            modifier = Modifier.size(24.dp), // TODO: [천민재] 20dp vs 24dp
            tint = PotiTheme.colors.sementicRed
        )
        Text(
            text = message,
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.sementicRed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiErrorMessagePreview() {
    PotiTheme {
        PotiErrorMessage(message = "오류메시지")
    }
}
