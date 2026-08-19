package com.poti.android.presentation.party.product.partylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.party.PartySummary
import com.poti.android.domain.model.party.ProductPartyList
import com.poti.android.presentation.party.component.MemberSelectBottomSheet
import com.poti.android.presentation.party.product.component.FilteredSortBottomSheet
import com.poti.android.presentation.party.product.component.PartyCard
import com.poti.android.presentation.party.product.membersText
import com.poti.android.presentation.party.product.partylist.model.PartySortType
import com.poti.android.presentation.party.product.partylist.model.ProductPartyListUiEffect
import com.poti.android.presentation.party.product.partylist.model.ProductPartyListUiIntent
import com.poti.android.presentation.party.product.priceText
import com.poti.android.presentation.party.product.ratingText

@Composable
fun ProductPartyListRoute(
    artistId: Long,
    onPopBackStack: () -> Unit,
    onNavigateToPartyCreate: (Long, String, String) -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductPartyListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            ProductPartyListUiEffect.NavigateBack -> onPopBackStack()
            is ProductPartyListUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate(artistId, effect.artistName, effect.productName)
            is ProductPartyListUiEffect.NavigateToPartyDetail -> onNavigateToPartyDetail(effect.partyId)
        }
    }

    if (uiState.isMemberFilterBottomSheetVisible) {
        MemberSelectBottomSheet(
            title = stringResource(R.string.goods_filter_member_select_label),
            onDismiss = { viewModel.processIntent(ProductPartyListUiIntent.CloseMemberFilterBottomSheet) },
            mainBtnText = stringResource(R.string.action_button_done),
            onMainBtnClick = { viewModel.processIntent(ProductPartyListUiIntent.OnMemberFilterDone) },
            mainEnabled = uiState.isMemberBottomSheetToucehd,
            subBtnText = stringResource(R.string.action_button_refresh),
            onSubBtnClick = { viewModel.processIntent(ProductPartyListUiIntent.OnMemberFilterRefresh) },
            subEnabled = true,
            allMembers = uiState.displayMembers,
            selectedMembers = uiState.bottomSheetSelectedMembers,
            onMemberClick = { viewModel.processIntent(ProductPartyListUiIntent.OnMemberSelect(it)) },
            memberToName = { it.name },
            memberToId = { it.memberId },
            autoCloseSubBtn = false,
        )
    }

    if (uiState.isSortFilterBottomSheetVisible) {
        FilteredSortBottomSheet(
            selectedSortType = uiState.partySortType,
            onSelect = { viewModel.processIntent(ProductPartyListUiIntent.OnSortSelect(it)) },
            onDismissRequest = { viewModel.processIntent(ProductPartyListUiIntent.CloseSortFilterBottomSheet) },
        )
    }

    ProductPartyListScreen(
        productPartyListInfo = uiState.productPartyListInfo.getSuccessDataOrNull() ?: ProductPartyList(
            partyTitle = uiState.cachedTitle,
            artistName = uiState.cachedSubTitle,
            partySummaries = emptyList(),
        ),
        partySortType = uiState.partySortType,
        memberFilterText = uiState.memberFilterText,
        isPageLoading = uiState.isPartyPageLoading,
        hasNextPage = uiState.hasNextPartyPage,
        onBackClick = { viewModel.processIntent(ProductPartyListUiIntent.OnBackClick) },
        onFloatingClick = { viewModel.processIntent(ProductPartyListUiIntent.OnFloatingClick) },
        onMemberFilterClick = { viewModel.processIntent(ProductPartyListUiIntent.OnMemberFilterClick) },
        onSortFilterClick = { viewModel.processIntent(ProductPartyListUiIntent.OnSortFilterClick) },
        onCardClick = { potId -> viewModel.processIntent(ProductPartyListUiIntent.OnPartyClick(potId)) },
        onLoadNextPage = { viewModel.processIntent(ProductPartyListUiIntent.LoadNextProductPartyList) },
        modifier = modifier,
    )
}

@Composable
private fun ProductPartyListScreen(
    productPartyListInfo: ProductPartyList,
    partySortType: PartySortType,
    memberFilterText: String,
    isPageLoading: Boolean,
    hasNextPage: Boolean,
    onBackClick: () -> Unit,
    onFloatingClick: () -> Unit,
    onMemberFilterClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onCardClick: (Long) -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember(listState, hasNextPage, isPageLoading) {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            hasNextPage &&
                !isPageLoading &&
                totalItemsCount > 0 &&
                lastVisibleItemIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            onLoadNextPage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PotiTheme.colors.white),
    ) {
        Column {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = productPartyListInfo.partyTitle,
                subTitle = productPartyListInfo.artistName,
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 76.dp,
                ),
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
                        imageUrl = party.productImageUrl,
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
            }
        }

        PotiFloatingButton(
            onClick = onFloatingClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 12.dp,
                ),
        )
    }
}

@Preview
@Composable
private fun ProductPartyListScreenPreveiw() {
    ProductPartyListScreen(
        productPartyListInfo = ProductPartyList(
            partyTitle = "헤더 타이틀",
            artistName = "서브타이틀",
            partySummaries = listOf(
                PartySummary(
                    partyId = 1,
                    price = 1000,
                    productImageUrl = "",
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
                    productImageUrl = "",
                    currentCount = 6,
                    totalCount = 6,
                    availableMembers = listOf("원영", "유진"),
                    profileImageUrl = "",
                    nickname = "닉네임",
                    rating = 1.2,
                ),
            ),
        ),
        partySortType = PartySortType.DEADLINE,
        memberFilterText = "",
        isPageLoading = false,
        hasNextPage = false,
        onBackClick = {},
        onFloatingClick = {},
        onMemberFilterClick = {},
        onSortFilterClick = {},
        onCardClick = {},
        onLoadNextPage = {},
    )
}
