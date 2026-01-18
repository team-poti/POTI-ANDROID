package com.poti.android.presentation.party.goodsfilter.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.model.party.Pots

enum class SortFilter(
    val request: String,
    val display: String,
) {
    LATEST(
        request = "LATEST",
        display = "최신순",
    ),
    DEADLINE(
        request = "DEADLINE",
        display = "마감임박순",
    ),
    RATING(
        request = "RATING",
        display = "평점순",
    ),
}

data class GoodsFilterUiState(
    val potsInfo: ApiState<Pots> = ApiState.Loading,
    val membersLoadState: ApiState<List<Member>> = ApiState.Loading,
    val selectedMember: List<Member> = emptyList(),
    val goodsSortFilter: SortFilter = SortFilter.LATEST,
) : UiState {
    val memberFilterText: String
        get() = when {
            selectedMember.isEmpty() -> "멤버 선택"
            selectedMember.size == 1 -> selectedMember[0].name
            selectedMember.size == 2 -> "${selectedMember[0].name}, ${selectedMember[1].name}"
            else -> "${selectedMember[0].name}, ${selectedMember[1].name} 외 ${selectedMember.size - 2}명"
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
    data object onMemberFilterClick
    data class onMembersSelect(val members: List<Member>)
    data object onSortFilterClick
    data class onSortSelect(val sort: SortFilter)
}

sealed interface GoodsFilterUiEffect : UiEffect {}
