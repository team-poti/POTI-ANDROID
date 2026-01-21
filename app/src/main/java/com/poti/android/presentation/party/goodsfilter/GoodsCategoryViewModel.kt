package com.poti.android.presentation.party.goodsfilter

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsCategoryViewModel @Inject constructor() :
    BaseViewModel<GoodsCategoryUiState, GoodsCategoryUiIntent, GoodsCategoryUiEffect>(
        initialState = GoodsCategoryUiState(),
    ) {
        override fun processIntent(intent: GoodsCategoryUiIntent) {
            when (intent) {
                GoodsCategoryUiIntent.OnBackClick -> sendEffect(GoodsCategoryUiEffect.NavigateBack)
                GoodsCategoryUiIntent.OnFloatingClick -> sendEffect(GoodsCategoryUiEffect.NavigateToPartyCreate)
                GoodsCategoryUiIntent.OnSortFilterClick -> {}
                GoodsCategoryUiIntent.OnCardClick -> sendEffect(GoodsCategoryUiEffect.NavigateToGoodsFilter)
            }
        }

        init {
            loadGoodsCategory()
        }

        private fun loadGoodsCategory() {
            updateState {
                copy(
                    goodsCategoryLoadState = ApiState.Success(
                        dummyGoodsCategory,
                    ),
                )
            }
        }
    }
