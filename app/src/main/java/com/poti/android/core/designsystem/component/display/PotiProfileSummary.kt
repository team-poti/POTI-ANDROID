package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class PotiProfileSummarySize(val profilePicSize: Dp) {
    SMALL(profilePicSize = 36.dp),
    LARGE(profilePicSize = 52.dp),
}

@Composable
fun PotiProfileSummary(
    profileImageUrl: String,
    nickname: String,
    sizeType: PotiProfileSummarySize,
    rating: String,
    showReview: Boolean,
    modifier: Modifier = Modifier,
    reviewText: String = "",
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SubcomposeAsyncImage(
            model = profileImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(sizeType.profilePicSize)
                .clip(RoundedCornerShape(99.dp)),
            // TODO: [천민재] 에셋 추가시 구현
            loading = {
                // TODO: [천민재] 임시 이미지
                Icon(
                    painter = painterResource(id = R.drawable.ic_member),
                    tint = Color.Black,
                    contentDescription = null,
                )
            },
            // TODO: [천민재] 에셋 추가시 구현
            error = {
            },
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = if (sizeType == PotiProfileSummarySize.LARGE && showReview) {
                Arrangement.spacedBy(4.dp)
            } else {
                Arrangement.Center
            },
        ) {
            if (sizeType == PotiProfileSummarySize.LARGE) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    NameAndRate(
                        nickname = nickname,
                        style = if (showReview) {
                            typography.body14sb
                        } else {
                            typography.body14m
                        },
                        rating = rating,
                    )
                }
            } else {
                Column {
                    NameAndRate(
                        nickname = nickname,
                        style = typography.body14m,
                        rating = rating,
                    )
                }
            }

            if (showReview && sizeType == PotiProfileSummarySize.LARGE) {
                Text(
                    text = reviewText,
                    style = typography.body14m,
                    color = colors.gray800,
                )
            }
        }
    }
}

@Composable
private fun NameAndRate(
    nickname: String,
    style: TextStyle,
    rating: String,
) {
    Text(
        text = nickname,
        style = style,
        color = colors.black,
    )
    PotiRating(rating = rating)
}

@Preview(showBackground = true)
@Composable
private fun PotiProfileSummaryPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                sizeType = PotiProfileSummarySize.SMALL,
                rating = "4.8",
                showReview = false,
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                sizeType = PotiProfileSummarySize.LARGE,
                rating = "4.8",
                showReview = false,
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                sizeType = PotiProfileSummarySize.LARGE,
                rating = "4.8",
                showReview = true,
                reviewText = "14개의 평가",
            )
        }
    }
}
