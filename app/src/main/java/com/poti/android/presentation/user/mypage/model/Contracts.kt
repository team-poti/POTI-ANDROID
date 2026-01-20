package com.poti.android.presentation.user.mypage.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.UserMyPage

data class MyPageUiState(
    val userMyPageLoadState: ApiState<UserMyPage> = ApiState.Loading,
) : UiState

sealed interface MyPageUiIntent : UiIntent {
    data object OnArtistClick : MyPageUiIntent
}

sealed interface MyPageUiEffect : UiEffect {
    data object NavigateToArtist : MyPageUiEffect
}
