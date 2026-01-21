package com.poti.android.presentation.user.profile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.UserRepository
import com.poti.android.presentation.user.profile.model.ProfileUiEffect
import com.poti.android.presentation.user.profile.model.ProfileUiIntent
import com.poti.android.presentation.user.profile.model.ProfileUiState
import com.poti.android.presentation.user.profile.navigation.ProfileRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProfileUiState, ProfileUiIntent, ProfileUiEffect>(
        initialState = ProfileUiState(),
    ) {
    private val userId = savedStateHandle.toRoute<ProfileRoute.Profile>().userId

    override fun processIntent(intent: ProfileUiIntent) {
        when (intent) {
            ProfileUiIntent.OnBackClick -> sendEffect(ProfileUiEffect.NavigateBack)
        }
    }

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() = launchScope {
        userRepository.getUserProfile(userId = userId)
            .onSuccess { userProfile ->
                updateState {
                    copy(userProfileLoadState = ApiState.Success(userProfile))
                }
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        userProfileLoadState = ApiState.Failure(throwable.message ?: "Failed"),
                    )
                }
            }
    }
}
