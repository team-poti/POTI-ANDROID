package com.poti.android.presentation.party.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiPrimaryTag
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagColor
import com.poti.android.core.designsystem.component.display.PotiPrimaryTagSize
import com.poti.android.core.designsystem.component.display.PotiSecondaryTag
import com.poti.android.core.designsystem.component.display.PotiTagSize
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun GoodsSmallCard(
    imageUrl: String,
    artist: String,
    goodsType: String,
    partyCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .heightIn(min = screenHeightDp(225.dp))
            .width(screenWidthDp(192.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.gray100)
            .border(
                width = 1.dp,
                color = PotiTheme.colors.gray300,
                shape = RoundedCornerShape(12.dp),
            )
            .noRippleClickable(onClick),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = Modifier
                .height(screenHeightDp(128.dp)),
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

            PotiSecondaryTag(
                text = "인기",
                sizeType = PotiTagSize.SMALL,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = artist,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            color = PotiTheme.colors.gray800,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = PotiTheme.typography.caption12m,
        )

        Text(
            text = goodsType,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            color = PotiTheme.colors.black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = PotiTheme.typography.body14m,
        )

        Spacer(Modifier.height(8.dp))

        PotiPrimaryTag(
            text = "팟 ${partyCount}개",
            sizeType = PotiPrimaryTagSize.LARGE,
            colorType = PotiPrimaryTagColor.WHITE,
            modifier = Modifier
                .padding(horizontal = 12.dp),
        )

        Spacer(Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodsSmallCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            GoodsSmallCard(
                imageUrl = "",
                artist = "아티스트명",
                goodsType = "상품 종류명",
                partyCount = 3,
                onClick = {},
                modifier = Modifier,
            )

            GoodsSmallCard(
                imageUrl = "",
                artist = "아티스트명 아티스트명 아티스트명 아티스트명 ",
                goodsType = "상품 종류명 상품 종류명 상품 종류명 ",
                partyCount = 3,
                onClick = {},
                modifier = Modifier,
            )
        }
    }
}
