package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiProfileSummarySize(val profilePicSize: Dp) {
    SMALL(profilePicSize = 36.dp),
    LARGE(profilePicSize = 52.dp)
}
@Composable
fun PotiProfileSummarySize.textStyle(showReview: Boolean): TextStyle {
    return when (this) {
        PotiProfileSummarySize.SMALL -> typography.body14m
        PotiProfileSummarySize.LARGE -> {
            if (showReview) typography.body14sb
            else typography.body14m
        }
    }
}

@Composable
fun PotiProfileSummary(
    profileImageUrl: String,
    nickname: String,
    size: PotiProfileSummarySize,
    rating: String,
    showReview: Boolean,
    reviewText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileImageUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(size.profilePicSize)
                .clip(RoundedCornerShape(99.dp)),
            loading = {
                PotiEmptyImagePlaceholder(modifier = Modifier.size(size.profilePicSize))
            },
            error = {
                PotiEmptyImagePlaceholder(modifier = Modifier.size(size.profilePicSize))
            }
        )

        val content: @Composable () -> Unit = {
            Text(
                text = nickname,
                style = size.textStyle(showReview),
                color = colors.black
            )
            PotiRating(rating = rating)
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = if (size == PotiProfileSummarySize.LARGE && showReview) Arrangement.spacedBy(4.dp)
            else Arrangement.Center
        ) {
            if (size == PotiProfileSummarySize.LARGE) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    content()
                }
            } else {
                Column {
                    content()
                }
            }

            if (showReview && size == PotiProfileSummarySize.LARGE) {
                Text(
                    text = reviewText,
                    style = typography.body14m,
                    color = colors.gray800
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PotiProfileSummaryPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                size = PotiProfileSummarySize.SMALL,
                rating = "4.8",
                showReview = false,
                reviewText = "",
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                size = PotiProfileSummarySize.LARGE,
                rating = "4.8",
                showReview = false,
                reviewText = "",
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                size = PotiProfileSummarySize.LARGE,
                rating = "4.8",
                showReview = true,
                reviewText = "14개의 평가"
            )
        }
    }
}
