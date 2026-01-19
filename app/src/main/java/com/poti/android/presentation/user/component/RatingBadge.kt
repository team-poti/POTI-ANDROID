package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.display.PotiRating
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun RatingBadge(
    rating: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .heightIn(min = 40.dp)
            .widthIn(min = 71.dp)
            .clip(CircleShape)
            .background(PotiTheme.colors.black)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PotiRating(
            rating = rating,
            iconTint = PotiTheme.colors.poti200,
            textColor = PotiTheme.colors.poti200,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingBadgePreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RatingBadge(
                rating = "4.8",
                modifier = Modifier.width(71.dp),
            )
        }
    }
}
