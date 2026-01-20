package com.poti.android.presentation.party.goodsfilter

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
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
        init {
            loadGoodsPots()
        }

        override fun processIntent(intent: GoodsFilterUiIntent) {
            when (intent) {
                GoodsFilterUiIntent.LoadGoodsPots -> loadGoodsPots()
                GoodsFilterUiIntent.OnBackClick -> sendEffect(GoodsFilterUiEffect.NavigateBack)
                GoodsFilterUiIntent.OnFloatingClick -> sendEffect(GoodsFilterUiEffect.NavigateToPartyCreate)
                is GoodsFilterUiIntent.OnPartyClick ->
                    sendEffect(GoodsFilterUiEffect.NavigateToPartyDetail(intent.partyId))
                GoodsFilterUiIntent.OnMemberFilterClick -> {
                    // TODO: [예림] 바텀시트 open
                }
                is GoodsFilterUiIntent.OnMembersSelect -> {
                    updateState { copy(selectedMember = intent.members) }
                }
                GoodsFilterUiIntent.OnSortFilterClick -> {
                    // TODO: [예림] 바텀시트 open
                }
                is GoodsFilterUiIntent.OnSortSelect -> {
                    updateState { copy(goodsSortFilter = intent.sort) }
                }
            }
        }

        private fun loadGoodsPots() = launchScope {
            updateState {
                copy(
                    partyListInfo = ApiState.Success(dummyPartyList),
                    membersLoadState = ApiState.Success(emptyList()),
                )
            }
        }
    }
