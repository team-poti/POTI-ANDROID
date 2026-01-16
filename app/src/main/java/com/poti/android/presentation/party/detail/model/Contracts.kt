@file:Suppress("ktlint:standard:filename")
package com.poti.android.presentation.party.detail.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.PartyDetail

data class PartyDetailUiState(
    val partyDetail: ApiState<PartyDetail> = ApiState.Loading,
) : UiState

sealed interface PartyDetailIntent : UiIntent {
    data object OnBackClick : PartyDetailIntent

    data object OnJoinClick : PartyDetailIntent

    data class OnUploaderClick(val userId: Long) : PartyDetailIntent

    data object LoadPartyDetail : PartyDetailIntent
}

sealed interface PartyDetailEffect : UiEffect {
    data object NavigateBack : PartyDetailEffect

    data object NavigateToJoin : PartyDetailEffect

    data class NavigateToProfile(val userId: Long) : PartyDetailEffect
}
