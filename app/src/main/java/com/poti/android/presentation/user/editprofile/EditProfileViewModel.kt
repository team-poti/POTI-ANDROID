package com.poti.android.presentation.user.editprofile

import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.domain.usecase.user.CheckNicknameDuplicationUseCase
import com.poti.android.domain.usecase.user.GetUserMyPageUseCase
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.user.editprofile.model.EditProfileUiEffect
import com.poti.android.presentation.user.editprofile.model.EditProfileUiIntent
import com.poti.android.presentation.user.editprofile.model.EditProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

private val NICKNAME_REGEX = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$".toRegex()

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserMyPageUseCase: GetUserMyPageUseCase,
    private val checkNicknameDuplicationUseCase: CheckNicknameDuplicationUseCase,
) : BaseViewModel<EditProfileUiState, EditProfileUiIntent, EditProfileUiEffect>(
        initialState = EditProfileUiState(),
    ) {
    private var originalNickname: String = ""

    override fun processIntent(intent: EditProfileUiIntent) {
        when (intent) {
            EditProfileUiIntent.OnBackClick -> sendEffect(EditProfileUiEffect.NavigateBack)
            is EditProfileUiIntent.OnProfileImageSelected -> {
                updateState { copy(selectedImageUri = intent.uri) }
            }
            is EditProfileUiIntent.OnNicknameChange -> handleNicknameChange(intent.value)
            // TODO: 닉네임/프로필 이미지 저장 API가 아직 없어서 처리 불가
            EditProfileUiIntent.OnSaveClick -> {}
        }
    }

    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() = launchScope {
        getUserMyPageUseCase()
            .onSuccess { userMyPage ->
                originalNickname = userMyPage.nickname
                updateState {
                    copy(
                        nickname = userMyPage.nickname,
                        profileImageUrl = userMyPage.profileImageUrl,
                        isNicknameValid = true,
                    )
                }
            }
            .onFailure { error ->
                Timber.e(error, "내 프로필 조회 실패")
            }
    }

    private fun handleNicknameChange(value: String) {
        val hasSpecialChar = !value.matches(NICKNAME_REGEX)

        val error: ErrorText? = when {
            value.length < 2 && value.isNotEmpty() -> ErrorText.StringResource(R.string.onboarding_nickname_error_min_length)
            hasSpecialChar -> ErrorText.StringResource(R.string.onboarding_nickname_error_special_characters)
            else -> null
        }

        updateState {
            copy(
                nickname = value,
                nicknameError = error,
                isNicknameValid = value == originalNickname,
            )
        }

        if (error == null && value.length >= 2 && value != originalNickname) {
            checkNicknameDuplication(value)
        }
    }

    private fun checkNicknameDuplication(nickname: String) = launchScope {
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
            }
            .onFailure {
                updateState { copy(nicknameError = ErrorText.StringResource(R.string.onboarding_nickname_error_server)) }
            }
    }
}
