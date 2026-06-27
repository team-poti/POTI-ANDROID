package com.poti.android.presentation.party.product.productcategory.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.ProductCategory

data class ProductCategoryUiState(
    val productCategoryLoadState: ApiState<ProductCategory> = ApiState.Loading,
    val isProductCategoryPageLoading: Boolean = false,
    val hasNextProductCategoryPage: Boolean = true,
    val nextProductCategoryPage: Int = 0,
    val isSortBottomSheetVisible: Boolean = false,
    val selectedSortType: ProductSortType = ProductSortType.HOT,
) : UiState

sealed interface ProductCategoryUiIntent : UiIntent {
    data object OnBackClick : ProductCategoryUiIntent

    data object OnFloatingClick : ProductCategoryUiIntent

    data object OnSortFilterClick : ProductCategoryUiIntent

    data class OnSortSelected(val sortType: ProductSortType) : ProductCategoryUiIntent

    data object OnLoadNextPage : ProductCategoryUiIntent

    data object OnSortDismiss : ProductCategoryUiIntent

    data class OnCardClick(val artistId: Long, val title: String) : ProductCategoryUiIntent
}

sealed interface ProductCategoryUiEffect : UiEffect {
    data object NavigateBack : ProductCategoryUiEffect

    data object NavigateToPartyCreate : ProductCategoryUiEffect

    data class NavigateToProductPartyList(val artistId: Long, val title: String) : ProductCategoryUiEffect
}
