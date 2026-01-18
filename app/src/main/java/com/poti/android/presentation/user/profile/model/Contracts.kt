package com.poti.android.presentation.user.profile.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.UserProfile

data class ProfileUiState(
    val userProfileLoadState: ApiState<UserProfile> = ApiState.Loading,
) : UiState

sealed interface ProfileUiIntent : UiIntent {
    data object OnBackClick : ProfileUiIntent
}

sealed interface ProfileUiEffect : UiEffect {
    data object NavigateBack : ProfileUiEffect
}
