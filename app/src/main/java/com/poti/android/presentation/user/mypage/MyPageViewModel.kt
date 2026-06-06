package com.poti.android.presentation.user.mypage

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.auth.WithdrawalUseCase
import com.poti.android.domain.usecase.user.GetUserMyPageUseCase
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect.*
import com.poti.android.presentation.user.mypage.model.MyPageUiIntent
import com.poti.android.presentation.user.mypage.model.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserMyPageUseCase: GetUserMyPageUseCase,
    private val withdrawalUseCase: WithdrawalUseCase,
) : BaseViewModel<MyPageUiState, MyPageUiIntent, MyPageUiEffect>(
        initialState = MyPageUiState(),
    ) {
    override fun processIntent(intent: MyPageUiIntent) {
        when (intent) {
            is MyPageUiIntent.OnHistoryClick -> {
                sendEffect(
                    NavigateToHistoryList(
                        mode = intent.mode,
                        tab = intent.tab,
                    ),
                )
            }
            MyPageUiIntent.OnMyArtistSelectClick -> launchScope {
                withdrawalUseCase()
                    .onSuccess { Timber.d("Withdrawal Success") }
                    .onFailure { e -> Timber.e(e, "Withdrawal Failed") }
            }
        }
    }

    init {
        loadUserMyPage()
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
