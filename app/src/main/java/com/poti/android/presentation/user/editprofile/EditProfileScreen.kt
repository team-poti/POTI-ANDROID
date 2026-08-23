package com.poti.android.presentation.user.editprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.user.component.EditableUserProfileImage
import com.poti.android.presentation.user.editprofile.model.EditProfileUiEffect
import com.poti.android.presentation.user.editprofile.model.EditProfileUiIntent

@Composable
fun EditProfileRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            EditProfileUiEffect.NavigateBack -> onPopBackStack()
        }
    }

    EditProfileScreen(
        profileImageUrl = uiState.selectedImageUri?.toString() ?: uiState.profileImageUrl,
        nickname = uiState.nickname,
        nicknameError = uiState.nicknameError,
        isSaveEnabled = uiState.isSaveEnabled,
        onBackClick = { viewModel.processIntent(EditProfileUiIntent.OnBackClick) },
        onProfileImageSelected = { uri -> viewModel.processIntent(EditProfileUiIntent.OnProfileImageSelected(uri)) },
        onNicknameChange = { value -> viewModel.processIntent(EditProfileUiIntent.OnNicknameChange(value)) },
        onSaveClick = { viewModel.processIntent(EditProfileUiIntent.OnSaveClick) },
        modifier = modifier,
    )
}

@Composable
private fun EditProfileScreen(
    profileImageUrl: String?,
    nickname: String,
    nicknameError: ErrorText?,
    isSaveEnabled: Boolean,
    onBackClick: () -> Unit,
    onProfileImageSelected: (Uri) -> Unit,
    onNicknameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        uri?.let(onProfileImageSelected)
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.profile_management_title),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                EditableUserProfileImage(
                    imageUrl = profileImageUrl,
                    onEditClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                .build(),
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp, bottom = 24.dp),
                )

                PotiShortTextField(
                    value = nickname,
                    onValueChanged = onNicknameChange,
                    label = stringResource(R.string.profile_nickname),
                    placeholder = stringResource(R.string.profile_nickname),
                    error = nicknameError?.asString() ?: "",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            PotiActionButton(
                text = stringResource(R.string.action_button_save),
                onClick = onSaveClick,
                enabled = isSaveEnabled,
                type = ActionButtonType.DEACTIVE_MAIN,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 14.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(
        profileImageUrl = null,
        nickname = "포티공주",
        nicknameError = null,
        isSaveEnabled = true,
        onBackClick = {},
        onProfileImageSelected = {},
        onNicknameChange = {},
        onSaveClick = {},
    )
}
