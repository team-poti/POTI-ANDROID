package com.poti.android.presentation.party.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.poti.android.core.designsystem.component.display.PotiPrimaryTag
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagColor
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagSize
import com.poti.android.core.designsystem.component.display.PotiSecondaryTag
import com.poti.android.core.designsystem.component.display.PotiTagSize
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun GoodsLargeCard(
    imageUrl: String,
    artist: String,
    artistId: Long,
    title: String,
    partyCount: Int,
    tag: String?,
    onClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.gray100)
            .border(
                width = 1.dp,
                color = PotiTheme.colors.gray300,
                shape = RoundedCornerShape(12.dp),
            )
            .noRippleClickable { onClick(artistId, title) },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(343f / 128f),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            if (!tag.isNullOrBlank()) {
                PotiSecondaryTag(
                    text = tag,
                    sizeType = PotiTagSize.LARGE,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = artist,
                    modifier = Modifier,
                    color = PotiTheme.colors.gray800,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PotiTheme.typography.body14m,
                )

                Text(
                    text = title,
                    modifier = Modifier,
                    color = PotiTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    minLines = 2,
                    style = PotiTheme.typography.body16m,
                )
            }

            PotiPrimaryTag(
                text = stringResource(R.string.goods_card_party_count, partyCount),
                sizeType = PotiPrimaryTagSize.SMALL,
                colorType = PotiPrimaryTagColor.WHITE,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodsLargeCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            GoodsLargeCard(
                imageUrl = "",
                artist = "아티스트명",
                artistId = 0L,
                title = "상품 종류명",
                partyCount = 3,
                tag = "인기",
                onClick = { _, _ -> },
                modifier = Modifier.fillMaxWidth(),
            )

            GoodsLargeCard(
                imageUrl = "",
                artist = "아티스트명 ".repeat(10),
                artistId = 0L,
                title = "상품 종류명 ".repeat(10),
                partyCount = 3,
                tag = "인기",
                onClick = { _, _ -> },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
