package com.poti.android.presentation.party.goodsfilter.model

import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.GoodsCategory

enum class GoodsSortType(
    val request: String,
    @StringRes val displayRes: Int,
) {
    LATEST(
        request = "LATEST",
        displayRes = R.string.goods_filter_sort_latest,
    ),
    HOT(
        request = "HOT",
        displayRes = R.string.goods_filter_sort_hot,
    ),
    RANDOM(
        request = "RANDOM ",
        displayRes = R.string.goods_filter_sort_random,
    ),
}

data class GoodsCategoryUiState(
    val goodsCategoryLoadState: ApiState<GoodsCategory> = ApiState.Loading,
    val isSortBottomSheetVisible: Boolean = false,
    val selectedSortType: GoodsSortType = GoodsSortType.LATEST,
) : UiState

sealed interface GoodsCategoryUiIntent : UiIntent {
    data object OnBackClick : GoodsCategoryUiIntent

    data object OnFloatingClick : GoodsCategoryUiIntent

    data object OnSortFilterClick : GoodsCategoryUiIntent

    data class OnSortSelected(val sortType: GoodsSortType) : GoodsCategoryUiIntent

    data object OnSortDismiss : GoodsCategoryUiIntent

    data object OnCardClick : GoodsCategoryUiIntent
}

sealed interface GoodsCategoryUiEffect : UiEffect {
    data object NavigateBack : GoodsCategoryUiEffect

    data object NavigateToPartyCreate : GoodsCategoryUiEffect

    data object NavigateToGoodsFilter : GoodsCategoryUiEffect
}
