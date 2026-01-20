package com.poti.android.presentation.party.home.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.home.HomeContent

data class HomeUiState(
    val homeContentLoadState: ApiState<HomeContent> = ApiState.Loading,
    val homeContent: HomeContent = HomeContent(),
) : UiState

sealed interface HomeUiIntent : UiIntent {
    data class OnBannerClick(val postId: Long) : HomeUiIntent

    data class OnMoreClick(val artistId: Long) : HomeUiIntent

    data class OnCardClick(val artistId: Long) : HomeUiIntent

    data object OnFloatingClick : HomeUiIntent

    data object LoadHomeContent : HomeUiIntent
}

sealed interface HomeUiEffect : UiEffect {
    data object NavigateToPartyCreate : HomeUiEffect

    data class NavigateToPartyDetail(val postId: Long) : HomeUiEffect

    data class NavigateToGoodsCategory(val artistId: Long) : HomeUiEffect

    data class NavigateToGoodsPartyList(val artistId: Long) : HomeUiEffect
}
