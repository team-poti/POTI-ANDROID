package com.poti.android.presentation.party.goodsfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.bottomsheet.MemberSelectBottomSheet
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.party.PartySummary
import com.poti.android.domain.model.party.ProductPartyList
import com.poti.android.presentation.party.goodsfilter.component.FilteredSortBottomSheet
import com.poti.android.presentation.party.goodsfilter.component.PartyCard
import com.poti.android.presentation.party.goodsfilter.model.FilteredSortType
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.membersText
import com.poti.android.presentation.party.goodsfilter.model.priceText
import com.poti.android.presentation.party.goodsfilter.model.ratingText

@Composable
fun GoodsFilteredPartyListRoute(
    artistId: Long,
    onPopBackStack: () -> Unit,
    onNavigateToPartyCreate: (Long, String, String) -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoodsFilterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            GoodsFilterUiEffect.NavigateBack -> onPopBackStack()
            is GoodsFilterUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate(artistId, effect.artistName, effect.productName)
            is GoodsFilterUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.partyId)
        }
    }

    if (uiState.isMemberFilterBottomSheetVisible) {
        MemberSelectBottomSheet(
            title = R.string.goods_filter_member_select_label,
            onDismiss = { viewModel.processIntent(GoodsFilterUiIntent.CloseMemberFilterBottomSheet) },
            mainBtnText = R.string.action_button_done,
            onMainBtnClick = { viewModel.processIntent(GoodsFilterUiIntent.OnMemberFilterDone) },
            mainEnabled = uiState.isMemberBottomSheetToucehd,
            subBtnText = R.string.action_button_refresh,
            onSubBtnClick = { viewModel.processIntent(GoodsFilterUiIntent.OnMemberFilterRefresh) },
            subEnabled = true,
            members = uiState.allMemberNames,
            selectedIndices = uiState.bottomSheetSelectedMembersIdices,
            onMemberClick = { viewModel.processIntent(GoodsFilterUiIntent.OnMemberSelect(it)) },
            autoCloseSubBtn = false,
        )
    }

    if (uiState.isSortFilterBottomSheetVisible) {
        FilteredSortBottomSheet(
            selectedSortType = uiState.goodsPartySortType,
            onSelect = { viewModel.processIntent(GoodsFilterUiIntent.OnSortSelect(it)) },
            onDismissRequest = { viewModel.processIntent(GoodsFilterUiIntent.CloseSortFilterBottomSheet) },
        )
    }

    GoodsFilteredPartyListScreen(
        productPartyListInfo = uiState.productPartyListInfo.getSuccessDataOrNull() ?: ProductPartyList(
            partyTitle = uiState.cachedTitle,
            artistName = uiState.cachedSubTitle,
            partySummaries = emptyList(),
        ),
        partySortType = uiState.goodsPartySortType,
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

@Composable
private fun GoodsFilteredPartyListScreen(
    productPartyListInfo: ProductPartyList,
    partySortType: FilteredSortType,
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
        contentWindowInsets = WindowInsets(),
        containerColor = PotiTheme.colors.white,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = productPartyListInfo.partyTitle,
                subTitle = productPartyListInfo.artistName,
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
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    PotiSmallButton(
                        text = memberFilterText,
                        onClick = onMemberFilterClick,
                    )

                    PotiSmallButton(
                        text = stringResource(partySortType.displayRes),
                        onClick = onSortFilterClick,
                    )
                }
            }

            items(productPartyListInfo.partySummaries) { party ->
                PartyCard(
                    potId = party.partyId,
                    profileImageUrl = party.profileImageUrl ?: "",
                    nickname = party.nickname,
                    rating = party.ratingText(),
                    imageUrl = party.goodsImageUrl,
                    members = party.membersText(),
                    price = party.priceText(),
                    currentCount = party.currentCount,
                    totalCount = party.totalCount,
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
        productPartyListInfo = ProductPartyList(
            partyTitle = "헤더 타이틀",
            artistName = "서브타이틀",
            partySummaries = listOf(
                PartySummary(
                    partyId = 1,
                    price = 1000,
                    goodsImageUrl = "",
                    currentCount = 5,
                    totalCount = 7,
                    availableMembers = listOf("원영", "유진", "이서"),
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = 1.2,
                ),
                PartySummary(
                    partyId = 1,
                    price = 8000,
                    goodsImageUrl = "",
                    currentCount = 6,
                    totalCount = 6,
                    availableMembers = listOf("원영", "유진"),
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = 1.2,
                ),
            ),
        ),
        partySortType = FilteredSortType.DEADLINE,
        memberFilterText = "",
        onBackClick = {},
        onFloatingClick = {},
        onMemberFilterClick = {},
        onSortFilterClick = {},
        onCardClick = {},
    )
}
