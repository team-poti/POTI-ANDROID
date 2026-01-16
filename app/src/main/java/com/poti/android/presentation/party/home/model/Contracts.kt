package com.poti.android.presentation.party.home.model

import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.home.HomeContent

data class HomeUiState(
    val homeContentLoadState: ApiState<HomeContent> = ApiState.Loading,
) : UiState
