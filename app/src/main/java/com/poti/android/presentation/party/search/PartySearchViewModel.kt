package com.poti.android.presentation.party.search

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.party.search.model.PartySearchUiEffect
import com.poti.android.presentation.party.search.model.PartySearchUiIntent
import com.poti.android.presentation.party.search.model.PartySearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartySearchViewModel @Inject constructor() : BaseViewModel<PartySearchUiState, PartySearchUiIntent, PartySearchUiEffect>(
    initialState = PartySearchUiState(),
) {
    override fun processIntent(intent: PartySearchUiIntent) {
        when (intent) {
            is PartySearchUiIntent.OnSearchKeywordChange -> updateState {
                copy(searchKeyword = intent.keyword)
            }
        }
    }
}
