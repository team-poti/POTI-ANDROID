@file:Suppress("ktlint:standard:filename")
package com.poti.android.presentation.party.detail.model

import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.PartyDetail

data class PartyDetailUiState(
    val partyDetail: ApiState<PartyDetail> = ApiState.Loading,
)
