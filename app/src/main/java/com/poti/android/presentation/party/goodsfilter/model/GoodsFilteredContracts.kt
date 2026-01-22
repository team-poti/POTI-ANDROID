package com.poti.android.presentation.party.goodsfilter.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.party.ProductPartyList

enum class PartySortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST("LATEST", R.string.goods_filter_sort_latest),
    HOT("HOT", R.string.goods_filter_sort_hot),
    RANDOM("RANDOM", R.string.goods_filter_sort_random),
}

data class GoodsFilterUiState(
    val productPartyListInfo: ApiState<ProductPartyList> = ApiState.Loading,
    val membersLoadState: ApiState<List<Member>> = ApiState.Loading,
    val displayMembers: List<Member> = emptyList(),
    val selectedMembers: List<Member> = emptyList(),
    val goodsPartySortType: PartySortType = PartySortType.HOT,
    val isMemberFilterBottomSheetVisible: Boolean = false,
    val isSortFilterBottomSheetVisible: Boolean = false,
    val bottomSheetSelectedMembersIdices: Set<Int> = setOf(),
    val isMemberBottomSheetToucehd: Boolean = false,
) : UiState {
    val allMemberNames: List<String>
        get() = displayMembers.map { it.name }

    val selectedMemberNames: List<String>
        get() = displayMembers.filterIndexed { index, _ -> index in bottomSheetSelectedMembersIdices }.map { it.name }

    val memberFilterText: String
        @Composable get() = when {
            selectedMembers.isEmpty() ->
                stringResource(R.string.goods_filter_member_select)

            selectedMembers.size == 1 ->
                selectedMembers[0].name

            selectedMembers.size == 2 ->
                stringResource(
                    R.string.goods_filter_member_two_format,
                    selectedMembers[0].name,
                    selectedMembers[1].name,
                )

            else ->
                stringResource(
                    R.string.goods_filter_member_more_format,
                    selectedMembers[0].name,
                    selectedMembers[1].name,
                    selectedMembers.size - 2,
                )
        }
}

sealed interface GoodsFilterUiIntent : UiIntent {
    data object LoadGoodsPots : GoodsFilterUiIntent

    data object OnBackClick : GoodsFilterUiIntent

    data object OnFloatingClick : GoodsFilterUiIntent

    data class OnPartyClick(val partyId: Long) : GoodsFilterUiIntent

    data object OnMemberFilterClick : GoodsFilterUiIntent

    data class OnMemberSelect(val index: Int) : GoodsFilterUiIntent

    data object OnSortFilterClick : GoodsFilterUiIntent

    data class OnSortSelect(val sort: PartySortType) : GoodsFilterUiIntent

    data object CloseMemberFilterBottomSheet : GoodsFilterUiIntent

    data object CloseSortFilterBottomSheet : GoodsFilterUiIntent

    data object OnMemberFilterDone : GoodsFilterUiIntent

    data object OnMemberFilterRefresh : GoodsFilterUiIntent
}

sealed interface GoodsFilterUiEffect : UiEffect {
    data object NavigateBack : GoodsFilterUiEffect

    data object NavigateToPartyCreate : GoodsFilterUiEffect

    data class NavigateToPartyDetail(val partyId: Long) : GoodsFilterUiEffect
}
