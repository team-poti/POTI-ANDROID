package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors

@Composable
fun PotiPagination(
    stage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PaginationDot(isSelected = stage == 1)
        PaginationDot(isSelected = stage == 2)
        PaginationDot(isSelected = stage == 3)
    }
}

@Composable
private fun PaginationDot(isSelected: Boolean) {
    val backgroundColor = if (isSelected) colors.poti200 else colors.gray300
    val width = if (isSelected) 16.dp else 6.dp

    Box(
        modifier = Modifier
            .size(width = width, height = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiPaginationPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PotiPagination(stage = 1)
            PotiPagination(stage = 2)
            PotiPagination(stage = 3)
        }
    }
}
