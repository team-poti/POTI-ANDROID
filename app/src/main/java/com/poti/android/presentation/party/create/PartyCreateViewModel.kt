package com.poti.android.presentation.party.create

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartyCreateViewModel @Inject constructor() : BaseViewModel<CreateUiState, CreateUiIntent, CreateUiEffect>(
    initialState = CreateUiState(),
) {
    override fun processIntent(intent: CreateUiIntent) {
        when (intent) {
            is CreateUiIntent.OnAccountNumberChange -> TODO()
            is CreateUiIntent.OnArtistSelect -> TODO()
            is CreateUiIntent.OnBankChange -> TODO()
            CreateUiIntent.OnCreateClick -> TODO()
            is CreateUiIntent.OnDeadlineChange -> TODO()
            is CreateUiIntent.OnDeliverySelect -> TODO()
            is CreateUiIntent.OnDescriptionChange -> TODO()
            is CreateUiIntent.OnImagesChanged -> TODO()
            CreateUiIntent.OnMemberEditClick -> TODO()
            is CreateUiIntent.OnMembersSelect -> TODO()
            is CreateUiIntent.OnPriceChange -> TODO()
            is CreateUiIntent.OnProductChange -> TODO()
            is CreateUiIntent.OnProductSelect -> TODO()
            CreateUiIntent.OnSearchClick -> TODO()
            CreateUiIntent.OnBackClick -> TODO()
        }
    }
}
