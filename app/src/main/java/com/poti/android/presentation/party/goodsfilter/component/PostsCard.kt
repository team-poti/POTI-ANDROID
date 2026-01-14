package com.poti.android.presentation.party.goodsfilter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiProfileSummary
import com.poti.android.core.designsystem.component.display.PotiProfileSummarySize
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PostsCard(
    profileImageUrl: String,
    nickname: String,
    rating: String,
    imageUrl: String,
    members: String,
    price: String,
    currentCount: Int,
    totalCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isClosed = totalCount - currentCount == 0
    val contentAlpha = if (isClosed) 0.5f else 1f

    Column(
        modifier = modifier
            .heightIn(min = 181.dp)
            .width(screenWidthDp(328.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white)
            .border(
                width = 1.dp,
                color = PotiTheme.colors.gray300,
                shape = RoundedCornerShape(12.dp),
            )
            .noRippleClickable(onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 42.dp)
                .fillMaxWidth()
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            PotiProfileSummary(
                profileImageUrl = profileImageUrl,
                nickname = nickname,
                sizeType = PotiProfileSummarySize.SMALL,
                rating = rating,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isClosed) {
                Text(
                    text = "마감",
                    color = PotiTheme.colors.gray800,
                    style = PotiTheme.typography.body16sb,
                )
            } else {
                Text(
                    text = "$currentCount",
                    color = PotiTheme.colors.sementicRed,
                    style = PotiTheme.typography.display18b,
                )
                Text(
                    text = "/$totalCount",
                    color = PotiTheme.colors.sementicRed,
                    style = PotiTheme.typography.body16sb,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        PotiDivider(
            styleType = PotiDividerStyle.SMALL,
            modifier = Modifier.alpha(contentAlpha),
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = members,
                    modifier = Modifier,
                    color = PotiTheme.colors.gray800,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = PotiTheme.typography.caption12m,
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = price,
                        modifier = Modifier,
                        color = PotiTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = PotiTheme.typography.display18b,
                    )
                    Text(
                        text = "/ 인",
                        modifier = Modifier,
                        color = PotiTheme.colors.gray800,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = PotiTheme.typography.body14m,
                    )
                }
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PotiTheme.colors.gray300)
                    .alpha(contentAlpha),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostsCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PostsCard(
                profileImageUrl = "",
                nickname = "닉네임",
                rating = "4.8",
                imageUrl = "",
                members = "남은멤버1 | 남은멤버2 | 남은멤버3 | 남은멤버4 | 남은멤버6 | 남은멤버7 | 남은멤버8 | 남은멤버9 | 남은멤버10",
                price = "5,000원~",
                onClick = {},
                currentCount = 6,
                totalCount = 7,
                modifier = Modifier,
            )

            PostsCard(
                profileImageUrl = "",
                nickname = "닉네임",
                rating = "4.8",
                imageUrl = "",
                members = "남은멤버1 | 남은멤버2 | 남은멤버3 | 남은멤버4 | 남은멤버6 | 남은멤버7",
                price = "5,000원~",
                onClick = {},
                currentCount = 7,
                totalCount = 7,
                modifier = Modifier,
            )
        }
    }
}
