package com.poti.android.presentation.onboarding

import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

private val NICKNAME_REGEX = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$".toRegex()

@HiltViewModel
class OnboardingViewModel @Inject constructor() : BaseViewModel<OnboardingUiState, OnboardingUiIntent, OnboardingUiEffect>(
    initialState = OnboardingUiState(),
) {
    init {
        fetchArtists()
    }

    override fun processIntent(intent: OnboardingUiIntent) {
        when (intent) {
            OnboardingUiIntent.OnBackClick -> sendEffect(OnboardingUiEffect.NavigateToBack)
            OnboardingUiIntent.OnGuideNextClick -> sendEffect(OnboardingUiEffect.NavigateToNickname)
            is OnboardingUiIntent.OnNicknameChange -> handleNicknameChange(intent.value)
            OnboardingUiIntent.OnNicknameNextClick -> {
                val currentState = uiState.value
                if (currentState.nicknameError == null && currentState.nickname.length >= 2) {
                    checkNicknameDuplication(currentState.nickname)
                }
            }
            is OnboardingUiIntent.OnArtistSelect -> handleArtistSelect(intent.artistId)
            OnboardingUiIntent.OnStartClick -> {
                // TODO: [지현] 서버에 데이터 보내는 로직 추가
                updateState { copy(isButtonVisible = false) }
                sendEffect(OnboardingUiEffect.NavigateToHome)
            }
            OnboardingUiIntent.OnSkipClick -> {
                updateState {
                    copy(
                        selectedArtistId = null,
                        isButtonVisible = false,
                    )
                }
                sendEffect(OnboardingUiEffect.NavigateToHome)
            }
        }
    }

    private fun fetchArtists() = launchScope {
        updateState { copy(artists = ApiState.Success(dummyArtists)) }
    }

    private fun handleNicknameChange(value: String) {
        val hasSpecialChar = !value.matches(NICKNAME_REGEX)

        val initialError: ErrorText? = when {
            value.length < 2 && value.isNotEmpty() -> ErrorText.StringResource(R.string.onboarding_nickname_error_min_length)
            hasSpecialChar -> ErrorText.StringResource(R.string.onboarding_nickname_error_special_characters)
            else -> null
        }

        updateState {
            copy(
                nickname = value,
                nicknameError = initialError,
                isNicknameValid = false,
            )
        }
    }

    private fun checkNicknameDuplication(nickname: String) = launchScope {
        Timber.d("닉네임 중복 및 비속어 확인 요청: $nickname")

        val isDuplicate = false
        val isProfanity = false

        if (isDuplicate) {
            // TODO: [지현] 닉네임 중복 처리
        } else if (isProfanity) {
            // TODO: [지현] 비속어 처리
        } else {
            updateState {
                copy(
                    nicknameError = null,
                    isNicknameValid = true,
                )
            }
            sendEffect(OnboardingUiEffect.NavigateToArtist)
        }
    }

    private fun handleArtistSelect(artistId: Long) {
        val currentSelectedId = uiState.value.selectedArtistId
        val newSelectedId = if (currentSelectedId == artistId) null else artistId
        updateState { copy(selectedArtistId = newSelectedId) }
    }
}
