package com.poti.android.presentation.onboarding

import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.network.model.NetworkError
import com.poti.android.domain.usecase.artist.GetArtistsUseCase
import com.poti.android.domain.usecase.user.CheckNicknameDuplicationUseCase
import com.poti.android.domain.usecase.user.SaveOnboardingUseCase
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

private val NICKNAME_REGEX = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$".toRegex()

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val checkNicknameDuplicationUseCase: CheckNicknameDuplicationUseCase,
    private val saveOnboardingUseCase: SaveOnboardingUseCase,
) : BaseViewModel<OnboardingUiState, OnboardingUiIntent, OnboardingUiEffect>(
        initialState = OnboardingUiState(),
    ) {
    override fun processIntent(intent: OnboardingUiIntent) {
        when (intent) {
            OnboardingUiIntent.OnBackClick -> sendEffect(OnboardingUiEffect.NavigateToBack)
            OnboardingUiIntent.OnGuideNextClick -> sendEffect(OnboardingUiEffect.NavigateToNickname)
            is OnboardingUiIntent.OnNicknameChange -> handleNicknameChange(intent.value)
            OnboardingUiIntent.OnNicknameNextClick -> handleNicknameNextClick()
            is OnboardingUiIntent.OnArtistSelect -> handleArtistSelect(intent.artistId)
            OnboardingUiIntent.OnStartClick -> handleStartClick()
            OnboardingUiIntent.OnSkipClick -> handleSkipClick()
        }
    }

    private fun fetchArtists() = launchScope {
        getArtistsUseCase()
            .onSuccess { artists ->
                updateState { copy(artists = ApiState.Success(artists.toImmutableList())) }
            }
            .onFailure { error ->
                updateState { copy(artists = ApiState.Failure(error.message ?: "Failed")) }
            }
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

        checkNicknameDuplicationUseCase(nickname)
            .onSuccess { isDuplicated ->
                if (isDuplicated) {
                    updateState { copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_duplicate)) }
                    return@onSuccess
                }
                updateState {
                    copy(
                        nicknameError = null,
                        isNicknameValid = true,
                    )
                }
                sendEffect(OnboardingUiEffect.NavigateToArtist)
                fetchArtists()
            }
            .onFailure { error ->
                if (error is NetworkError.BadRequest) {
                    when (error.code) {
                        40003 -> {
                            updateState { copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_duplicate)) }
                        }
                        else -> {
                            updateState { copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_special_characters)) }
                        }
                    }
                } else {
                    updateState { copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_server)) }
                }
            }
    }

    private fun handleNicknameNextClick() {
        val currentState = uiState.value

        if (currentState.nicknameError == null && currentState.nickname.length >= 2) {
            checkNicknameDuplication(currentState.nickname)
        } else if (currentState.nickname.length < 2) {
            updateState {
                copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_min_length))
            }
        }
    }

    private fun handleArtistSelect(artistId: Long) {
        val currentSelectedId = uiState.value.selectedArtistId
        val newSelectedId = if (currentSelectedId == artistId) null else artistId
        updateState { copy(selectedArtistId = newSelectedId) }
    }

    private fun handleStartClick() = launchScope {
        val artistId = uiState.value.selectedArtistId

        artistId?.let {
            saveOnboardingUseCase(uiState.value.nickname, artistId)
                .onSuccess {
                    updateState { copy(isButtonVisible = false) }
                    sendEffect(OnboardingUiEffect.NavigateToHome)
                }
                .onFailure { error ->
                    Timber.e(error, "온보딩 저장 실패")
                }
        }
    }

    private fun handleSkipClick() = launchScope {
        saveOnboardingUseCase(uiState.value.nickname, null)
            .onSuccess { }
        updateState {
            copy(
                selectedArtistId = null,
                isButtonVisible = false,
            )
        }
        sendEffect(OnboardingUiEffect.NavigateToHome)
    }
}
