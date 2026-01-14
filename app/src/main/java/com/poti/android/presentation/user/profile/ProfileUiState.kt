package com.poti.android.presentation.user.profile

data class ProfileUiState(
    val imageUrl: String = "",
    val nickname: String = "",
    val email: String = "",
    val rating: String = "",
    val infoList: List<String> = emptyList(),
    val recruitHistory: HistorySummaryUiModel = HistorySummaryUiModel(
        totalCount = 0,
        inProgressCount = 0,
        finishedCount = 0,
    ),
)
