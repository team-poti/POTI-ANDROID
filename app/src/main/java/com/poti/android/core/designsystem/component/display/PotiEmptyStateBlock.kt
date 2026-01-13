package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiEmptyStateBlock(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Icon(
            // TODO: [천민재] 빈 이미지 추가 필요
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delivery),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(180.dp),
        )
        Text(
            text = text,
            style = PotiTheme.typography.body16m,
            color = PotiTheme.colors.gray700,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiEmptyStateBlockPreview() {
    PotiTheme {
        PotiEmptyStateBlock(text = "텍스트")
    }
}
