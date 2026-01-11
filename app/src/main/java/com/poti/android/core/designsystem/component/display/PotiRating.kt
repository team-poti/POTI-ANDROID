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
import com.poti.android.core.designsystem.theme.Gray800
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiRating(
    rating: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Gray800
        )
        Text( // TODO: [천민재] 14dp x 1.5em = 21dp, img: 20dp => 중앙 배열 어긋남
            text = rating,
            style = PotiTheme.typography.body14m,
            color = Gray800,
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
