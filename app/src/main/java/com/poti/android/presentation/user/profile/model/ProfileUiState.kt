package com.poti.android.presentation.user.profile.model

import com.poti.android.domain.model.HistorySummaryItem

data class ProfileUiState(
    val imageUrl: String = "",
    val nickname: String = "",
    val email: String = "",
    val rating: String = "",
    val infoList: List<String> = emptyList(),
    val recruitHistoryItems: List<HistorySummaryItem> = emptyList(),
)
