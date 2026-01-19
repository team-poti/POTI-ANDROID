package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiRating(
    rating: String,
    modifier: Modifier = Modifier,
    iconTint: Color = PotiTheme.colors.gray800,
    textColor: Color = PotiTheme.colors.gray800,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.size(21.dp),
            tint = iconTint,
        )
        Text(
            text = rating,
            style = PotiTheme.typography.body14m,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun PotiRatingPreview() {
    PotiTheme {
        PotiRating(rating = "4.8")
    }
}
