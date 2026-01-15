package com.poti.android.presentation.onboarding.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Artist
import kotlinx.collections.immutable.ImmutableList

data class OnboardingUiState(
    val nickname: String = "",
    val nicknameError: ErrorText? = null,
    val isNicknameValid: Boolean = false,
    val artists: ApiState<ImmutableList<Artist>> = ApiState.Loading,
    val selectedArtistId: Long = 0,
    val isButtonVisible: Boolean = true,
) : UiState

sealed interface OnboardingUiIntent : UiIntent {
    data object OnBackClick : OnboardingUiIntent

    data object OnGuideNextClick : OnboardingUiIntent

    data class OnNicknameChange(val value: String) : OnboardingUiIntent

    data object OnNicknameNextClick : OnboardingUiIntent

    data class OnArtistClick(val artistId: Long) : OnboardingUiIntent

    data object OnArtistNextClick : OnboardingUiIntent
}

sealed interface OnboardingUiEffect : UiEffect {
    data object NavigateToBack : OnboardingUiEffect

    data object NavigateToNickname : OnboardingUiEffect

    data object NavigateToArtist : OnboardingUiEffect

    data object NavigateToHome : OnboardingUiEffect
}
