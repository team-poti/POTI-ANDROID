package com.poti.android.presentation.user.mypage

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.user.GetUserMyPageUseCase
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect.*
import com.poti.android.presentation.user.mypage.model.MyPageUiIntent
import com.poti.android.presentation.user.mypage.model.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserMyPageUseCase: GetUserMyPageUseCase,
) : BaseViewModel<MyPageUiState, MyPageUiIntent, MyPageUiEffect>(
        initialState = MyPageUiState(),
    ) {
    override fun processIntent(intent: MyPageUiIntent) {
        when (intent) {
            MyPageUiIntent.OnArtistClick -> handleArtistClick()
            MyPageUiIntent.OnResume -> loadUserMyPage()
            is MyPageUiIntent.OnHistoryClick -> {
                sendEffect(
                    NavigateToHistoryList(
                        mode = intent.mode,
                        tab = intent.tab,
                    ),
                )
            }
        }
    }

    init {
        loadUserMyPage()
    }

    private fun handleArtistClick() {
        val userMyPage = (uiState.value.userMyPageLoadState as? ApiState.Success)?.data ?: return

        if (userMyPage.hasFavoriteArtist) {
            // TODO: [천민재] 최애가 이미 있을 때의 동작은 기획 확인 후 처리 예정
            return
        }

        sendEffect(NavigateToFavoriteArtist)
    }

    private fun loadUserMyPage() = launchScope {
        getUserMyPageUseCase()
            .onSuccess { userMyPage ->
                updateState {
                    copy(userMyPageLoadState = ApiState.Success(userMyPage))
                }
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        userMyPageLoadState = ApiState.Failure(throwable.message ?: "Failed"),
                    )
                }
            }
    }
}
