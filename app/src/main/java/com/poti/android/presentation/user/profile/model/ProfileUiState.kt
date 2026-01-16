package com.poti.android.presentation.user.profile.model

import com.poti.android.presentation.user.component.HistorySummaryItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProfileUiState(
    val imageUrl: String = "",
    val nickname: String = "",
    val email: String = "",
    val rating: String = "",
    val infoList: ImmutableList<String> = persistentListOf(),
    val recruitHistoryItems: ImmutableList<HistorySummaryItem> = persistentListOf(),
)
