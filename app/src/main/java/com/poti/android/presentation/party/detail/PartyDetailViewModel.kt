package com.poti.android.presentation.party.detail

import androidx.lifecycle.ViewModel
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(PartyDetailUiState())
    val uiState = _uiState.asStateFlow()
}
