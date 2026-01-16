package com.poti.android.presentation.party.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.core.designsystem.component.display.PotiPagination
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.home.model.BannerUiModel

@Composable
fun HomeBannerSection(
    banners: List<BannerUiModel>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { banners.size }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(328f / 196f),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    rotationZ = -4.26f
                },
        ) {
            AsyncImage(
                model = banners[pagerState.currentPage].imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PotiTheme.colors.gray100),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp)),
        ) { page ->
            AsyncImage(
                model = banners[page].imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    // .clip(RoundedCornerShape(12.dp))
                    .background(PotiTheme.colors.gray100),
                contentScale = ContentScale.Crop,
            )
        }

        PotiPagination(
            maxSize = banners.size,
            stage = pagerState.currentPage + 1,
            modifier = Modifier.padding(bottom = 22.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBannerSectionPreview() {
    PotiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            HomeBannerSection(
                banners = listOf(
                    BannerUiModel(1, ""),
                    BannerUiModel(2, ""),
                    BannerUiModel(3, ""),
                ),
                modifier = Modifier,
            )
        }
    }
}
