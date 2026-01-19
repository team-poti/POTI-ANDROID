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
            is CreateUiIntent.OnAccountNumberChange -> {}
            is CreateUiIntent.OnArtistSelect -> {}
            is CreateUiIntent.OnBankChange -> {}
            CreateUiIntent.OnCreateClick -> {}
            is CreateUiIntent.OnDeadlineChange -> {}
            is CreateUiIntent.OnDeliverySelect -> {}
            is CreateUiIntent.OnDescriptionChange -> {}
            is CreateUiIntent.OnImagesChanged -> {}
            CreateUiIntent.OnMemberEditClick -> {}
            is CreateUiIntent.OnMembersSelect -> {}
            is CreateUiIntent.OnPriceChange -> {}
            is CreateUiIntent.OnProductChange -> {}
            is CreateUiIntent.OnProductSelect -> {}
            CreateUiIntent.OnSearchClick -> {}
            CreateUiIntent.OnBackClick -> {}
        }
    }
}
