package com.poti.android.presentation.user.profile

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.presentation.user.profile.model.ProfileUiEffect
import com.poti.android.presentation.user.profile.model.ProfileUiIntent
import com.poti.android.presentation.user.profile.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel<ProfileUiState, ProfileUiIntent, ProfileUiEffect>(
    initialState = ProfileUiState(),
) {
    override fun processIntent(intent: ProfileUiIntent) {
        when (intent) {
            ProfileUiIntent.OnBackClick -> sendEffect(ProfileUiEffect.NavigateBack)
        }
    }

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        updateState {
            copy(
                userProfileLoadState = ApiState.Success(
                    UserProfile(
                        userId = 1L,
                        email = "akkma@app.jam",
                        nickname = "분철의 악마",
                        profileImageUrl = "",
                        ratingAvg = 4.8,
                        activityMessage = "최근 3일 이내 활동",
                        joinedAt = "2025-12-28",
                        hasFavoriteArtist = true,
                        recruitSummary = HistorySummary(
                            total = 7,
                            inProgress = 2,
                            completed = 5,
                        ),
                    ),
                ),
            )
        }
    }
}
