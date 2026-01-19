package com.poti.android.presentation.user.mypage

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect
import com.poti.android.presentation.user.mypage.model.MyPageUiIntent
import com.poti.android.presentation.user.mypage.model.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : BaseViewModel<MyPageUiState, MyPageUiIntent, MyPageUiEffect>(
    initialState = MyPageUiState(),
) {
    override fun processIntent(intent: MyPageUiIntent) {
        when (intent) {
            MyPageUiIntent.OnArtistClick -> sendEffect(MyPageUiEffect.NavigateToArtist)
        }
    }

    init {
        loadUserMyPage()
    }

    private fun loadUserMyPage() {
        updateState {
            copy(
                userMyPageLoadState = ApiState.Success(
                    UserMyPage(
                        nickname = "분철의 악마",
                        email = "akkma@app.jam",
                        profileImageUrl = "",
                        ratingAvg = "4.8",
                        activityMessage = "최근 3일 이내 활동",
                        joinedAt = "2025-12-28",
                        hasFavoriteArtist = true,
                        favoriteArtistName = null,
                        participationSummary = HistorySummary(
                            total = 12,
                            inProgress = 3,
                            completed = 9,
                        ),
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
