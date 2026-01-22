package com.poti.android.presentation.party.home.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiTextButton
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.home.GroupItem
import com.poti.android.presentation.party.home.fakeMyGroupItems

@Composable
fun HomeGoodsSection(
    artistId: Long,
    @StringRes title: Int,
    nickname: String,
    groupItems: List<GroupItem>,
    onMoreClick: (Long) -> Unit,
    onCardClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = screenWidthDp(16.dp),
                    end = screenWidthDp(4.dp),
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(title, nickname),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.body16sb,
            )

            PotiTextButton(
                text = stringResource(R.string.home_more),
                onClick = { onMoreClick(artistId) },
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = screenWidthDp(16.dp)),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(groupItems) { item ->
                GoodsSmallCard(
                    imageUrl = item.postImage,
                    artist = item.artist,
                    artistId = item.artistId,
                    title = item.postTitle,
                    partyCount = item.postCount,
                    tag = item.tag,
                    onClick = { id, title -> onCardClick(id, title) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGoodsSectionPreview() {
    PotiTheme {
        HomeGoodsSection(
            artistId = 0L,
            title = R.string.home_recommend_goods,
            nickname = "포티",
            groupItems = fakeMyGroupItems,
            onMoreClick = {},
            onCardClick = { id, title -> },
        )
    }
}
