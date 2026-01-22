package com.poti.android.presentation.party.product.productcategory

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.presentation.party.product.navigation.ProductRoute.ProductCategory
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiEffect
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiIntent
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiState
import com.poti.android.presentation.party.product.productcategory.model.ProductSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<ProductCategoryUiState, ProductCategoryUiIntent, ProductCategoryUiEffect>(
            initialState = ProductCategoryUiState(),
        ) {
        private val args = savedStateHandle.toRoute<ProductCategory>()
        private val artistId: Long? = args.artistId
        val isMyArtist: Boolean = args.isMyArtist

        override fun processIntent(intent: ProductCategoryUiIntent) {
            when (intent) {
                ProductCategoryUiIntent.OnBackClick -> sendEffect(ProductCategoryUiEffect.NavigateBack)
                ProductCategoryUiIntent.OnFloatingClick -> sendEffect(ProductCategoryUiEffect.NavigateToPartyCreate)
                ProductCategoryUiIntent.OnSortFilterClick -> updateState { copy(isSortBottomSheetVisible = true) }
                is ProductCategoryUiIntent.OnSortSelected -> {
                    updateState {
                        copy(
                            selectedSortType = intent.sortType,
                            isSortBottomSheetVisible = false,
                        )
                    }
                    loadGoodsCategoryList(intent.sortType)
                }

                ProductCategoryUiIntent.OnSortDismiss -> updateState { copy(isSortBottomSheetVisible = false) }
                is ProductCategoryUiIntent.OnCardClick -> sendEffect(ProductCategoryUiEffect.NavigateToProductPartyList(intent.artistId, intent.title))
            }
        }

        init {
            loadGoodsCategoryList()
        }

        private fun loadGoodsCategoryList(sortType: ProductSortType = uiState.value.selectedSortType) =
            launchScope {
                updateState { copy(productCategoryLoadState = ApiState.Loading) }

                homeRepository.getGoodsCategoryList(
                    page = 0,
                    size = 100,
                    sort = sortType.name,
                    artistId = artistId,
                )
                    .onSuccess { goodsCategory ->
                        updateState {
                            copy(productCategoryLoadState = ApiState.Success(goodsCategory))
                        }
                    }
                    .onFailure { throwable ->
                        updateState {
                            copy(
                                productCategoryLoadState = ApiState.Failure(
                                    throwable.message ?: "Failed to load goods category",
                                ),
                            )
                        }
                    }
            }
    }
