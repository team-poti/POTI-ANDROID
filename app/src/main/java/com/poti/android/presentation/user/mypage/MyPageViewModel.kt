package com.poti.android.presentation.user.mypage

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.UserRepository
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect
import com.poti.android.presentation.user.mypage.model.MyPageUiEffect.*
import com.poti.android.presentation.user.mypage.model.MyPageUiIntent
import com.poti.android.presentation.user.mypage.model.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
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
                authRepository.withdrawal()
                    .onSuccess { Timber.d("Withdrawal Success") }
                    .onFailure { e -> Timber.e(e, "Withdrawal Failed") }
            }
        }
    }

    init {
        loadUserMyPage()
    }

    private fun loadUserMyPage() = launchScope {
        userRepository.getUserMyPage()
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
