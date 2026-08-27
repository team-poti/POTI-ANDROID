package com.poti.android.presentation.user.editprofile

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.NicknameValidator
import com.poti.android.domain.type.ImageUploadType
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.user.CheckNicknameDuplicationUseCase
import com.poti.android.domain.usecase.user.EditProfileUseCase
import com.poti.android.domain.usecase.user.GetUserMyPageUseCase
import com.poti.android.presentation.user.editprofile.model.EditProfileUiEffect
import com.poti.android.presentation.user.editprofile.model.EditProfileUiIntent
import com.poti.android.presentation.user.editprofile.model.EditProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserMyPageUseCase: GetUserMyPageUseCase,
    private val checkNicknameDuplicationUseCase: CheckNicknameDuplicationUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val editProfileUseCase: EditProfileUseCase,
) : BaseViewModel<EditProfileUiState, EditProfileUiIntent, EditProfileUiEffect>(
        initialState = EditProfileUiState(),
    ) {
    private var nicknameCheckJob: Job? = null

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
        nicknameCheckJob?.cancel()

        val originalNickname = uiState.value.originalNickname
        val error = NicknameValidator.validateFormat(value)

        updateState {
            copy(
                nickname = value,
                nicknameError = error,
                isNicknameValid = value == originalNickname,
            )
        }

        if (error == null && value.length >= NicknameValidator.MIN_LENGTH && value != originalNickname) {
            checkNicknameDuplication(value)
        }
    }

    private fun handleSaveClick() {
        val currentState = uiState.value
        if (currentState.saveState is ApiState.Loading) return

        updateState { copy(saveState = ApiState.Loading) }

        launchScope(
            onError = { error ->
                updateState { copy(saveState = ApiState.Failure(error.message ?: "Failed")) }
            },
        ) {
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
    }

    private fun checkNicknameDuplication(nickname: String) {
        nicknameCheckJob = viewModelScope.launch {
            checkNicknameDuplicationUseCase(nickname)
                .onSuccess { isDuplicated ->
                    if (uiState.value.nickname != nickname) return@onSuccess

                    if (isDuplicated) {
                        updateState { copy(nicknameError = NicknameValidator.duplicateNicknameError()) }
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
                    if (uiState.value.nickname != nickname) return@onFailure

                    updateState { copy(nicknameError = NicknameValidator.toDuplicationCheckError(error)) }
                }
        }
    }
}
