package com.poti.android.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private val NICKNAME_REGEX = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$".toRegex()

@HiltViewModel
class OnboardingViewModel @Inject constructor() : BaseViewModel<OnboardingUiState, OnboardingUiIntent, OnboardingUiEffect>(
    initialState = OnboardingUiState(),
) {
    private val nicknameInputChannel = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        viewModelScope.launch {
            nicknameInputChannel
                .debounce(500L) // 입력 멈추고 0.5초 대기
                .distinctUntilChanged() // 이전과 동일한 값이면 무시
                .filter { it.length >= 2 } // 2글자 미만이면 서버 요청 안 함
                .collectLatest { nickname ->
                    checkNicknameDuplication(nickname)
                }
        }

        fetchArtists()
    }

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
            is OnboardingUiIntent.OnArtistClick -> handleArtistSelect(intent.artistId)
            OnboardingUiIntent.OnArtistNextClick -> {
                updateState { copy(isButtonVisible = false) }
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

        viewModelScope.launch {
            nicknameInputChannel.emit(value)
        }
    }

    private fun checkNicknameDuplication(nickname: String) = launchScope {
        Timber.d("닉네임 중복 및 비속어 확인")

        val isDuplicate = false
        val isProfanity = false

        if (isDuplicate) {
            // TODO: [지현] 닉네임 중복 처리
        } else if (isProfanity) {
            // TODO: [지현] 비속서 처리
        } else {
            updateState {
                copy(
                    nicknameError = null,
                    isNicknameValid = true,
                )
            }
        }
    }

    private fun handleArtistSelect(artistId: Long) {
        updateState { copy(selectedArtistId = artistId) }
    }
}
