package com.poti.android.presentation.onboarding

import com.poti.android.core.base.BaseViewModel
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : BaseViewModel<OnboardingUiState, OnboardingUiIntent, OnboardingUiEffect>(
    initialState = OnboardingUiState(),
) {
    override fun processIntent(intent: OnboardingUiIntent) {
        when (intent) {
            OnboardingUiIntent.OnBackClick -> sendEffect(OnboardingUiEffect.NavigateToBack)
            OnboardingUiIntent.OnGuideNextClick -> sendEffect(OnboardingUiEffect.NavigateToNickname)
            is OnboardingUiIntent.OnNicknameChange -> handleNicknameChange(intent.value)
            OnboardingUiIntent.OnNicknameNextClick -> {
                if (uiState.value.isNicknameValid) {
                    sendEffect(OnboardingUiEffect.NavigateToArtist)
                }
            }
            is OnboardingUiIntent.OnArtistSelect -> handleArtistSelect(intent.artistId)
            OnboardingUiIntent.OnArtistNextClick -> {
                updateState { copy(isButtonVisible = false) }
                sendEffect(OnboardingUiEffect.NavigateToHome)
            }
        }
    }

    private fun handleNicknameChange(value: String) {
        // TODO: [지현] 유효성 검사 로직
    }

    private fun handleArtistSelect(artistId: Long) {
        updateState { copy(selectedArtistId = artistId) }
    }
}
