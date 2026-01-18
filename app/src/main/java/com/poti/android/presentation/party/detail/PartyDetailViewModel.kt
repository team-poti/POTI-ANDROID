package com.poti.android.presentation.party.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailUiState
import com.poti.android.presentation.party.detail.navigation.PartyDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PartyDetailUiState, PartyDetailIntent, PartyDetailEffect>(
        initialState = PartyDetailUiState(),
    ) {
    private val args = savedStateHandle.toRoute<PartyDetailRoute.Detail>()
    private val recruitId = args.recruitId

    init {
        processIntent(PartyDetailIntent.LoadPartyDetail)
    }

    override fun processIntent(intent: PartyDetailIntent) {
        when (intent) {
            PartyDetailIntent.LoadPartyDetail -> loadPartyDetail()
            PartyDetailIntent.OnBackClick -> sendEffect(PartyDetailEffect.NavigateBack)
            PartyDetailIntent.OnJoinClick -> sendEffect(PartyDetailEffect.NavigateToJoin)
            is PartyDetailIntent.OnUploaderClick -> sendEffect(PartyDetailEffect.NavigateToProfile(intent.userId))
        }
    }

    private fun loadPartyDetail() = launchScope {
        updateState { copy(partyDetail = ApiState.Loading) }
        // TODO: [지현] 나중에 서버 연결
        updateState { copy(partyDetail = ApiState.Success(dummyPartyDetail)) }
    }
}
