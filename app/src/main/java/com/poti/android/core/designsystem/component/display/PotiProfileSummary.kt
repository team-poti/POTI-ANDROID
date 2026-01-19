package com.poti.android.core.designsystem.component.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    profileImageUrl: String?,
    nickname: String,
    sizeType: PotiProfileSummarySize,
    rating: String,
    modifier: Modifier = Modifier,
    reviewText: String? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(sizeType.profilePicSize)
                .clip(RoundedCornerShape(99.dp)),
            placeholder = painterResource(id = R.drawable.img_basic_profile),
            error = painterResource(id = R.drawable.img_basic_profile),
        )

        when (sizeType) {
            PotiProfileSummarySize.LARGE -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    NameAndRate(
                        nickname = nickname,
                        style = if (reviewText != null) {
                            typography.body14sb
                        } else {
                            typography.body14m
                        },
                        rating = rating,
                        isVertical = false,
                    )
                    if (reviewText != null) {
                        Text(
                            text = reviewText,
                            style = typography.body14m,
                            color = colors.gray800,
                        )
                    }
                }
            }

            PotiProfileSummarySize.SMALL -> {
                NameAndRate(
                    nickname = nickname,
                    style = typography.body14m,
                    rating = rating,
                    isVertical = true,
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
    isVertical: Boolean = false,
) {
    if (isVertical) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = nickname,
                    style = style,
                    color = colors.black,
                )
            }
            PotiRating(rating = rating)
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = nickname,
                style = style,
                color = colors.black,
            )
            PotiRating(rating = rating)
        }
    }
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
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                sizeType = PotiProfileSummarySize.LARGE,
                rating = "4.8",
            )
            PotiProfileSummary(
                profileImageUrl = "",
                nickname = "닉네임",
                sizeType = PotiProfileSummarySize.LARGE,
                rating = "4.8",
                reviewText = "14개의 평가",
            )
        }
    }
}
