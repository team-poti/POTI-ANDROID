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
    maxSize: Int,
    stage: Int,
    modifier: Modifier = Modifier,
) {
    val index = if (stage > maxSize) {
        maxSize
    } else if (stage < 1) {
        1
    } else {
        stage
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..maxSize) {
            PaginationDot(isSelected = index == i)
        }
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
            .background(backgroundColor),
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiPaginationPreview() {
    PotiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PotiPagination(maxSize = 3, stage = 1)
            PotiPagination(maxSize = 4, stage = 2)
            PotiPagination(maxSize = 5, stage = 3)
            PotiPagination(maxSize = 5, stage = 6)
            PotiPagination(maxSize = 5, stage = -1)
        }
    }
}
