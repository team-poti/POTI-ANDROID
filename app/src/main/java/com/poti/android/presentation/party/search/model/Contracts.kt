package com.poti.android.presentation.party.search.model

import androidx.compose.runtime.Immutable
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.search.PartySearchResult

@Immutable
data class PartySearchUiState(
    val searchKeyword: String = "",
    val searchResultLoadState: ApiState<PartySearchResult> = ApiState.Init,
    val isPageLoading: Boolean = false,
    val hasNextPage: Boolean = false,
    val nextPage: Int = 0,
) : UiState {}

sealed interface PartySearchUiIntent : UiIntent {
    data class OnSearchKeywordChange(val keyword: String) : PartySearchUiIntent

    data class OnSearch(val keyword: String) : PartySearchUiIntent

    data object OnLoadNextPage : PartySearchUiIntent
}

sealed interface PartySearchUiEffect : UiEffect {
}
