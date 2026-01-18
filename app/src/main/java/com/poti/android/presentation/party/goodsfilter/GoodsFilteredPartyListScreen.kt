package com.poti.android.presentation.party.goodsfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.party.PotSummary
import com.poti.android.domain.model.party.Pots
import com.poti.android.presentation.party.goodsfilter.component.PotsCard
import com.poti.android.presentation.party.goodsfilter.model.SortFilter

@Composable
fun GoodsFilteredPartyListRoute(
    artistId: Long,
    modifier: Modifier = Modifier,
) {
//    GoodsFilteredPartyListScreen(modifier = modifier)
}

@Composable
private fun GoodsFilteredPartyListScreen(
    potsInfo: Pots,
    selectedMember: List<Member>,
    sortFilter: SortFilter,
    memberFilterText: String,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = PotiTheme.colors.white,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = {},
                title = potsInfo.postTitle,
                subTitle = potsInfo.artistName,
            )
        },
        floatingActionButton = {
            PotiFloatingButton(
                onClick = {},
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = screenWidthDp(16.dp)),
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PotiSmallButton(
                        text = memberFilterText,
                        onClick = {},
                    )

                    PotiSmallButton(
                        text = stringResource(sortFilter.displayRes),
                        onClick = {},
                    )
                }
            }

            items(potsInfo.potSummaries) { pot ->
                PotsCard(
                    profileImageUrl = pot.profileImageUrl ?: "",
                    nickname = pot.nickname,
                    rating = pot.rating,
                    imageUrl = pot.goodsImageUrl,
                    members = pot.availableMembers,
                    price = pot.price,
                    currentCount = pot.currentCount,
                    totalCount = pot.totalCount,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                )
            }

            item {
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Preview
@Composable
private fun GoodsFilteredPartyListScreenPreveiw() {
    GoodsFilteredPartyListScreen(
        potsInfo = Pots(
            postTitle = "헤더 타이틀",
            artistName = "서브타이틀",
            potSummaries = listOf(
                PotSummary(
                    potId = 1,
                    price = "1,000~",
                    goodsImageUrl = "",
                    currentCount = 5,
                    totalCount = 7,
                    availableMembers = "원영, 유진",
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = "1.2",
                ),
                PotSummary(
                    potId = 1,
                    price = "1,000",
                    goodsImageUrl = "",
                    currentCount = 6,
                    totalCount = 6,
                    availableMembers = "원영, 유진",
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = "1.2",
                ),
            ),
        ),
        selectedMember = emptyList(),
        sortFilter = SortFilter.LATEST,
        memberFilterText = "",
    )
}
