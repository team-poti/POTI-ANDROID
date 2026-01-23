package com.poti.android.presentation.party.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiProfileSummary
import com.poti.android.core.designsystem.component.display.PotiProfileSummarySize
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PartyCard(
    potId: Long,
    profileImageUrl: String,
    nickname: String,
    rating: String,
    imageUrl: String,
    members: String,
    price: String,
    currentCount: Int,
    totalCount: Int,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isClosed = totalCount - currentCount == 0
    val contentAlpha = if (isClosed) 0.5f else 1f

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white)
            .border(
                width = 1.dp,
                color = PotiTheme.colors.gray300,
                shape = RoundedCornerShape(12.dp),
            )
            .noRippleClickable { onClick(potId) }
            .padding(16.dp)
            .alpha(contentAlpha),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    text = stringResource(R.string.pots_card_closed),
                    color = PotiTheme.colors.gray800,
                    style = PotiTheme.typography.body16sb,
                )
            } else {
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.pots_card_current_count, currentCount),
                        color = PotiTheme.colors.sementicRed,
                        style = PotiTheme.typography.display18b,
                    )
                    Text(
                        text = stringResource(R.string.pots_card_total_count, totalCount),
                        color = PotiTheme.colors.sementicRed,
                        style = PotiTheme.typography.body16sb,
                    )
                }
            }
        }

        PotiDivider(styleType = PotiDividerStyle.SMALL)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
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
                    color = PotiTheme.colors.gray800,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = PotiTheme.typography.caption12m,
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = price,
                        color = PotiTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = PotiTheme.typography.display18b,
                    )

                    Text(
                        text = stringResource(R.string.pots_card_per_person),
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
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PotiTheme.colors.gray300),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PartyCard(
                potId = 0L,
                profileImageUrl = "",
                nickname = "닉네임",
                rating = "4.8",
                imageUrl = "",
                members = "남은멤버1 | 남은멤버2 | 남은멤버3 | 남은멤버4 | 남은멤버6 | 남은멤버7 | 남은멤버8 | 남은멤버9 | 남은멤버10",
                price = "5,000원~",
                onClick = {},
                currentCount = 6,
                totalCount = 7,
                modifier = Modifier.fillMaxWidth(),
            )

            PartyCard(
                potId = 0L,
                profileImageUrl = "",
                nickname = "닉네임",
                rating = "4.8",
                imageUrl = "",
                members = "남은멤버1 | 남은멤버2 | 남은멤버3 | 남은멤버4 | 남은멤버6 | 남은멤버7",
                price = "5,000원~",
                onClick = {},
                currentCount = 7,
                totalCount = 7,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
