package com.poti.android.presentation.party.product.partylist.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.party.ProductPartyList

data class ProductPartyListUiState(
    val productPartyListInfo: ApiState<ProductPartyList> = ApiState.Loading,
    val cachedTitle: String = "",
    val cachedSubTitle: String = "",
    val membersLoadState: ApiState<List<Member>> = ApiState.Loading,
    val displayMembers: List<Member> = emptyList(),
    val selectedMembers: List<Member> = emptyList(),
    val partySortType: PartySortType = PartySortType.DEADLINE,
    val isMemberFilterBottomSheetVisible: Boolean = false,
    val isSortFilterBottomSheetVisible: Boolean = false,
    val bottomSheetSelectedMembers: List<Member> = emptyList(),
    val isMemberBottomSheetToucehd: Boolean = false,
) : UiState {
    val memberFilterText: String
        @Composable get() = when {
            selectedMembers.isEmpty() ->
                stringResource(R.string.party_filter_member_select)

            selectedMembers.size == 1 ->
                selectedMembers[0].name

            selectedMembers.size == 2 ->
                stringResource(
                    R.string.party_filter_member_two_format,
                    selectedMembers[0].name,
                    selectedMembers[1].name,
                )

            else ->
                stringResource(
                    R.string.party_filter_member_more_format,
                    selectedMembers[0].name,
                    selectedMembers[1].name,
                    selectedMembers.size - 2,
                )
        }
}

sealed interface ProductPartyListUiIntent : UiIntent {
    data object LoadProductPartyList : ProductPartyListUiIntent

    data object OnBackClick : ProductPartyListUiIntent

    data object OnFloatingClick : ProductPartyListUiIntent

    data class OnPartyClick(val partyId: Long) : ProductPartyListUiIntent

    data object OnMemberFilterClick : ProductPartyListUiIntent

    data class OnMemberSelect(val member: Member) : ProductPartyListUiIntent

    data object OnSortFilterClick : ProductPartyListUiIntent

    data class OnSortSelect(val sort: PartySortType) : ProductPartyListUiIntent

    data object CloseMemberFilterBottomSheet : ProductPartyListUiIntent

    data object CloseSortFilterBottomSheet : ProductPartyListUiIntent

    data object OnMemberFilterDone : ProductPartyListUiIntent

    data object OnMemberFilterRefresh : ProductPartyListUiIntent
}

sealed interface ProductPartyListUiEffect : UiEffect {
    data object NavigateBack : ProductPartyListUiEffect

    data class NavigateToPartyCreate(val artistName: String, val productName: String) : ProductPartyListUiEffect

    data class NavigateToPartyDetail(val partyId: Long) : ProductPartyListUiEffect
}
