package com.poti.android.presentation.user.editprofile

import com.poti.android.R
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.network.model.NetworkError
import com.poti.android.domain.type.ImageUploadType
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.user.CheckNicknameDuplicationUseCase
import com.poti.android.domain.usecase.user.EditProfileUseCase
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
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val editProfileUseCase: EditProfileUseCase,
) : BaseViewModel<EditProfileUiState, EditProfileUiIntent, EditProfileUiEffect>(
        initialState = EditProfileUiState(),
    ) {
    override fun processIntent(intent: EditProfileUiIntent) {
        when (intent) {
            EditProfileUiIntent.OnBackClick -> sendEffect(EditProfileUiEffect.NavigateBack)
            is EditProfileUiIntent.OnProfileImageSelected -> {
                updateState { copy(selectedImageUri = intent.uri) }
            }
            is EditProfileUiIntent.OnNicknameChange -> handleNicknameChange(intent.value)
            EditProfileUiIntent.OnSaveClick -> handleSaveClick()
        }
    }

    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() = launchScope {
        getUserMyPageUseCase()
            .onSuccess { userMyPage ->
                updateState {
                    copy(
                        originalNickname = userMyPage.nickname,
                        nickname = userMyPage.nickname,
                        profileImageUrl = userMyPage.profileImageUrl,
                        savedProfileImageUri = null,
                        selectedImageUri = null,
                        isNicknameValid = true,
                    )
                }
            }
            .onFailure { error ->
                Timber.e(error, "내 프로필 조회 실패")
            }
    }

    private fun handleNicknameChange(value: String) {
        val originalNickname = uiState.value.originalNickname
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

    private fun handleSaveClick() = launchScope {
        val currentState = uiState.value
        updateState { copy(saveState = ApiState.Loading) }

        val uploadedFileName = currentState.selectedImageUri?.let { uri ->
            uploadImagesUseCase(
                uploadType = ImageUploadType.PROFILE,
                uriStrings = listOf(uri.toString()),
            ).getOrElse { error ->
                updateState { copy(saveState = ApiState.Failure(error.message ?: "Failed")) }
                return@launchScope
            }.first()
        }

        editProfileUseCase(
            nickname = currentState.nickname,
            profileImageUrl = uploadedFileName ?: currentState.profileImageUrl.orEmpty(),
        ).onSuccess {
            updateState {
                copy(
                    originalNickname = currentState.nickname,
                    profileImageUrl = uploadedFileName ?: currentState.profileImageUrl,
                    savedProfileImageUri = currentState.selectedImageUri ?: currentState.savedProfileImageUri,
                    selectedImageUri = null,
                    saveState = ApiState.Success(Unit),
                )
            }
        }.onFailure { error ->
            updateState { copy(saveState = ApiState.Failure(error.message ?: "Failed")) }
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
}
