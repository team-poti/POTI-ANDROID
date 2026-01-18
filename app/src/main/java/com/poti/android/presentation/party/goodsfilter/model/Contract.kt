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
import com.poti.android.domain.model.party.Pots

enum class SortFilter(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST(
        request = "LATEST",
        displayRes = R.string.goods_filter_sort_latest,
    ),
    DEADLINE(
        request = "DEADLINE",
        displayRes = R.string.goods_filter_sort_deadline,
    ),
    RATING(
        request = "RATING",
        displayRes = R.string.goods_filter_sort_rating,
    ),
}

data class GoodsFilterUiState(
    val potsInfo: ApiState<Pots> = ApiState.Loading,
    val membersLoadState: ApiState<List<Member>> = ApiState.Loading,
    val selectedMember: List<Member> = emptyList(),
    val goodsSortFilter: SortFilter = SortFilter.LATEST,
) : UiState {
    val memberFilterText: String
        @Composable get() = when {
            selectedMember.isEmpty() -> stringResource(R.string.goods_filter_member_select)
            selectedMember.size == 1 -> selectedMember[0].name
            selectedMember.size == 2 -> stringResource(
                R.string.goods_filter_member_two_format,
                selectedMember[0].name,
                selectedMember[1].name,
            )
            else -> stringResource(
                R.string.goods_filter_member_more_format,
                selectedMember[0].name,
                selectedMember[1].name,
                selectedMember.size - 2,
            )
        }

    val loadState: ApiState<Unit>
        get() = when {
            potsInfo is ApiState.Loading &&
                membersLoadState is ApiState.Loading -> ApiState.Loading

            potsInfo is ApiState.Failure ||
                membersLoadState is ApiState.Failure -> ApiState.Failure("")

            potsInfo is ApiState.Success &&
                membersLoadState is ApiState.Success -> ApiState.Success(Unit)

            else -> ApiState.Loading
        }
}

sealed interface GoodsFilterUiIntent : UiIntent {
    data object LoadGoodsPots : GoodsFilterUiIntent

    data object OnBackClick : GoodsFilterUiIntent

    data object OnFloatingClick : GoodsFilterUiIntent

    data class OnPartyClick(val potId: Long) : GoodsFilterUiIntent

    data object OnMemberFilterClick : GoodsFilterUiIntent

    data class OnMembersSelect(val members: List<Member>) : GoodsFilterUiIntent

    data object OnSortFilterClick : GoodsFilterUiIntent

    data class OnSortSelect(val sort: SortFilter) : GoodsFilterUiIntent
}

sealed interface GoodsFilterUiEffect : UiEffect {
    data object NavigateBack : GoodsFilterUiEffect

    data object NavigateToPartyCreate : GoodsFilterUiEffect

    data class NavigateToPartyDetail(val userId: Long) : GoodsFilterUiEffect
}
