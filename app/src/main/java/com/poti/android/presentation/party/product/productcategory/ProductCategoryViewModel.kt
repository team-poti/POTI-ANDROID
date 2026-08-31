package com.poti.android.presentation.party.product.productcategory

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.auth.IsGuestUseCase
import com.poti.android.domain.usecase.home.GetGoodsCategoryListUseCase
import com.poti.android.presentation.party.product.navigation.ProductRoute.ProductCategory
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiEffect
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiIntent
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiState
import com.poti.android.presentation.party.product.productcategory.model.ProductSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val PRODUCT_CATEGORY_PAGE_SIZE = 10

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val getGoodsCategoryListUseCase: GetGoodsCategoryListUseCase,
    private val isGuestUseCase: IsGuestUseCase,
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
                ProductCategoryUiIntent.OnFloatingClick -> handleFloatingClick()
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

                ProductCategoryUiIntent.OnLoadNextPage -> loadGoodsCategoryList(reset = false)
                ProductCategoryUiIntent.OnSortDismiss -> updateState { copy(isSortBottomSheetVisible = false) }
                is ProductCategoryUiIntent.OnCardClick -> sendEffect(ProductCategoryUiEffect.NavigateToProductPartyList(intent.artistId, intent.title))
                ProductCategoryUiIntent.OnLoginRequiredConfirm -> handleLoginRequiredConfirm()
                ProductCategoryUiIntent.OnLoginRequiredDismiss -> updateState { copy(showLoginRequiredDialog = false) }
            }
        }

        private fun handleFloatingClick() {
            if (isGuestUseCase()) {
                updateState { copy(showLoginRequiredDialog = true) }
            } else {
                sendEffect(ProductCategoryUiEffect.NavigateToPartyCreate)
            }
        }

        private fun handleLoginRequiredConfirm() {
            updateState { copy(showLoginRequiredDialog = false) }
            sendEffect(ProductCategoryUiEffect.NavigateToLogin)
        }

        init {
            loadGoodsCategoryList()
        }

        private fun loadGoodsCategoryList(
            sortType: ProductSortType = uiState.value.selectedSortType,
            reset: Boolean = true,
        ) =
            launchScope {
                val state = uiState.value
                if (!reset && (state.isProductCategoryPageLoading || !state.hasNextProductCategoryPage)) return@launchScope

                val page = if (reset) 0 else state.nextProductCategoryPage

                updateState {
                    copy(
                        productCategoryLoadState = if (reset) ApiState.Loading else productCategoryLoadState,
                        isProductCategoryPageLoading = true,
                    )
                }

                getGoodsCategoryListUseCase(
                    page = page,
                    size = PRODUCT_CATEGORY_PAGE_SIZE,
                    sort = sortType.name,
                    artistId = artistId,
                )
                    .onSuccess { goodsCategory ->
                        val currentGroupItems = (uiState.value.productCategoryLoadState as? ApiState.Success)
                            ?.data
                            ?.groupItems
                            .orEmpty()
                        val updatedGroupItems = if (reset) {
                            goodsCategory.groupItems
                        } else {
                            currentGroupItems + goodsCategory.groupItems
                        }

                        updateState {
                            copy(
                                productCategoryLoadState = ApiState.Success(
                                    goodsCategory.copy(
                                        groupItems = updatedGroupItems.distinctBy { item ->
                                            item.artistId to item.postTitle
                                        },
                                    ),
                                ),
                                isProductCategoryPageLoading = false,
                                hasNextProductCategoryPage = goodsCategory.groupItems.size == PRODUCT_CATEGORY_PAGE_SIZE,
                                nextProductCategoryPage = page + 1,
                            )
                        }
                    }
                    .onFailure { throwable ->
                        updateState {
                            copy(
                                productCategoryLoadState = if (reset) {
                                    ApiState.Failure(throwable.message ?: "Failed to load goods category")
                                } else {
                                    productCategoryLoadState
                                },
                                isProductCategoryPageLoading = false,
                            )
                        }
                    }
            }
    }
