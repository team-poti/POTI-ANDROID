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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.party.PotSummary
import com.poti.android.domain.model.party.Pots
import com.poti.android.presentation.party.goodsfilter.component.PotsCard
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.SortFilter

@Composable
fun GoodsFilteredPartyListRoute(
    artistId: Long,
    onPopBackStack: () -> Unit,
    onNavigateToPartyCreate: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoodsFilterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            GoodsFilterUiEffect.NavigateBack -> onPopBackStack()
            GoodsFilterUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate()
            is GoodsFilterUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.userId)
        }
    }

    uiState.potsInfo.onSuccess { potsInfo ->
        GoodsFilteredPartyListScreen(
            potsInfo = potsInfo,
            selectedMember = uiState.selectedMember,
            sortFilter = uiState.goodsSortFilter,
            memberFilterText = uiState.memberFilterText,
            onBackClick = {
                viewModel.processIntent(GoodsFilterUiIntent.OnBackClick)
            },
            onFloatingClick = {
                viewModel.processIntent(GoodsFilterUiIntent.OnFloatingClick)
            },
            onMemberFilterClick = {
                viewModel.processIntent(GoodsFilterUiIntent.OnMemberFilterClick)
            },
            onSortFilterClick = {
                viewModel.processIntent(GoodsFilterUiIntent.OnSortFilterClick)
            },
            onCardClick = { potId ->
                viewModel.processIntent(GoodsFilterUiIntent.OnPartyClick(potId))
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun GoodsFilteredPartyListScreen(
    potsInfo: Pots,
    selectedMember: List<Member>,
    sortFilter: SortFilter,
    memberFilterText: String,
    onBackClick: () -> Unit,
    onFloatingClick: () -> Unit,
    onMemberFilterClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onCardClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = PotiTheme.colors.white,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = potsInfo.postTitle,
                subTitle = potsInfo.artistName,
            )
        },
        floatingActionButton = {
            PotiFloatingButton(
                onClick = onFloatingClick, // TODO: [예림] 등록 화면(PartyCreateScreen)에 (아티스트 + 굿즈명) 넘겨주기
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
                        onClick = onMemberFilterClick,
                    )

                    PotiSmallButton(
                        text = stringResource(sortFilter.displayRes),
                        onClick = onSortFilterClick,
                    )
                }
            }

            items(potsInfo.potSummaries) { pot ->
                PotsCard(
                    potId = pot.potId,
                    profileImageUrl = pot.profileImageUrl ?: "",
                    nickname = pot.nickname,
                    rating = pot.ratingText,
                    imageUrl = pot.goodsImageUrl,
                    members = pot.availableMembers,
                    price = pot.priceText,
                    currentCount = pot.currentCount,
                    totalCount = pot.totalCount,
                    onClick = onCardClick,
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
                    price = 1000,
                    goodsImageUrl = "",
                    currentCount = 5,
                    totalCount = 7,
                    availableMembers = "원영, 유진",
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = 1.2,
                ),
                PotSummary(
                    potId = 1,
                    price = 8000,
                    goodsImageUrl = "",
                    currentCount = 6,
                    totalCount = 6,
                    availableMembers = "원영, 유진",
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = 1.2,
                ),
            ),
        ),
        selectedMember = emptyList(),
        sortFilter = SortFilter.LATEST,
        memberFilterText = "",
        onBackClick = {},
        onFloatingClick = {},
        onMemberFilterClick = {},
        onSortFilterClick = {},
        onCardClick = {},
    )
}
