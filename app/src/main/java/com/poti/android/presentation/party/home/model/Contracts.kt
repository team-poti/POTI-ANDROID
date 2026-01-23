package com.poti.android.presentation.party.home.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.home.HomeContent

data class HomeUiState(
    val homeContentLoadState: ApiState<HomeContent> = ApiState.Loading,
    val artistIdToNull: Boolean = false,
) : UiState

sealed interface HomeUiIntent : UiIntent {
    data class OnMyArtistCategoryClick(val artistId: Long?) : HomeUiIntent

    data class OnProductCardClick(val artistId: Long, val title: String) : HomeUiIntent

    data object OnOtherProductCategoryClick : HomeUiIntent

    data object OnFloatingClick : HomeUiIntent

    data object LoadHomeContent : HomeUiIntent
}

sealed interface HomeUiEffect : UiEffect {
    data object NavigateToPartyCreate : HomeUiEffect

    data class NavigateToMyArtistCategory(val artistId: Long?) : HomeUiEffect

    data object NavigateToOtherProductCategory : HomeUiEffect

    data class NavigateToGoodsPartyList(val artistId: Long, val title: String) : HomeUiEffect
}
