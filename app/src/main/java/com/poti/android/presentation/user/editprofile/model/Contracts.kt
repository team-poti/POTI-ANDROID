package com.poti.android.presentation.user.editprofile.model

import android.net.Uri
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.presentation.onboarding.model.ErrorText

data class EditProfileUiState(
    val profileImageUrl: String? = null,
    val selectedImageUri: Uri? = null,
    val nickname: String = "",
    val nicknameError: ErrorText? = null,
    val isNicknameValid: Boolean = false,
    val saveState: ApiState<Unit> = ApiState.Init,
) : UiState {
    val isSaveEnabled: Boolean
        get() = isNicknameValid && nickname.isNotBlank()
}

sealed interface EditProfileUiIntent : UiIntent {
    data object OnBackClick : EditProfileUiIntent

    data class OnProfileImageSelected(val uri: Uri) : EditProfileUiIntent

    data class OnNicknameChange(val value: String) : EditProfileUiIntent

    data object OnSaveClick : EditProfileUiIntent
}

sealed interface EditProfileUiEffect : UiEffect {
    data object NavigateBack : EditProfileUiEffect
}
