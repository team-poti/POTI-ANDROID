package com.poti.android.presentation.party.goodsfilter

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsFilterViewModel @Inject constructor() :
    BaseViewModel<GoodsFilterUiState, GoodsFilterUiIntent, GoodsFilterUiEffect>(
        initialState = GoodsFilterUiState(),
    ) {
        override fun processIntent(intent: GoodsFilterUiIntent) {
            TODO("Not yet implemented")
        }
    }
