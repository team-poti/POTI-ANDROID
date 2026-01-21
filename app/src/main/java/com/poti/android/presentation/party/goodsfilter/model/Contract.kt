package com.poti.android.presentation.party.goodsfilter.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.PartyList

enum class PartySortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST("LATEST", R.string.goods_filter_sort_latest),
    DEADLINE("DEADLINE", R.string.goods_filter_sort_deadline),
    RATING("RATING", R.string.goods_filter_sort_rating),
}

data class FilterMember(
    val id: Long,
    val name: String,
)

data class GoodsFilterUiState(
    val partyListInfo: ApiState<PartyList> = ApiState.Loading,
    val membersLoadState: ApiState<List<FilterMember>> = ApiState.Loading,
    val displayMembers: List<FilterMember> = emptyList(),
    val selectedMembers: List<FilterMember> = emptyList(),
    val goodsPartySortType: PartySortType = PartySortType.LATEST,
) : UiState {
    val selectedMemberIds: List<Long>
        get() = selectedMembers.map { it.id }

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

    val loadState: ApiState<Unit>
        get() = when {
            partyListInfo is ApiState.Loading &&
                membersLoadState is ApiState.Loading -> ApiState.Loading

            partyListInfo is ApiState.Failure ||
                membersLoadState is ApiState.Failure -> ApiState.Failure("")

            partyListInfo is ApiState.Success &&
                membersLoadState is ApiState.Success -> ApiState.Success(Unit)

            else -> ApiState.Loading
        }
}

sealed interface GoodsFilterUiIntent : UiIntent {
    data object LoadGoodsPots : GoodsFilterUiIntent

    data object OnBackClick : GoodsFilterUiIntent

    data object OnFloatingClick : GoodsFilterUiIntent

    data class OnPartyClick(val partyId: Long) : GoodsFilterUiIntent

    data object OnMemberFilterClick : GoodsFilterUiIntent

    data class OnMembersSelect(val members: List<FilterMember>) : GoodsFilterUiIntent

    data object OnSortFilterClick : GoodsFilterUiIntent

    data class OnSortSelect(val sort: PartySortType) : GoodsFilterUiIntent
}

sealed interface GoodsFilterUiEffect : UiEffect {
    data object NavigateBack : GoodsFilterUiEffect

    data object NavigateToPartyCreate : GoodsFilterUiEffect

    data class NavigateToPartyDetail(val partyId: Long) : GoodsFilterUiEffect
}
