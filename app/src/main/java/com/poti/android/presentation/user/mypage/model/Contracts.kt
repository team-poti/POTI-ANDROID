package com.poti.android.presentation.user.mypage.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.presentation.history.list.HistoryMode
import com.poti.android.presentation.user.component.HistorySummaryType

data class MyPageUiState(
    val userMyPageLoadState: ApiState<UserMyPage> = ApiState.Loading,
) : UiState

sealed interface MyPageUiIntent : UiIntent {
    data object OnArtistClick : MyPageUiIntent

    data class OnHistoryClick(
        val mode: HistoryMode,
        val tab: HistorySummaryType,
    ) : MyPageUiIntent
}

sealed interface MyPageUiEffect : UiEffect {
    data object NavigateToArtist : MyPageUiEffect

    data class NavigateToHistoryList(
        val mode: HistoryMode,
        val tab: HistorySummaryType,
    ) : MyPageUiEffect
}
