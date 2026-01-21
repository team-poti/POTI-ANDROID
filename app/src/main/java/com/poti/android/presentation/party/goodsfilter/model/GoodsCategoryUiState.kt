package com.poti.android.presentation.party.goodsfilter.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.GoodsCategory

data class GoodsCategoryUiState(
    val goodsCategoryLoadState: ApiState<GoodsCategory> = ApiState.Loading,
) : UiState

sealed interface GoodsCategoryUiIntent : UiIntent {
    data object OnBackClick : GoodsCategoryUiIntent

    data object OnFloatingClick : GoodsCategoryUiIntent

    data object OnSortFilterClick : GoodsCategoryUiIntent

    data object OnCardClick : GoodsCategoryUiIntent
}

sealed interface GoodsCategoryUiEffect : UiEffect {
    data object NavigateBack : GoodsCategoryUiEffect

    data object NavigateToPartyCreate : GoodsCategoryUiEffect

    data object NavigateToGoodsFilter : GoodsCategoryUiEffect
}
