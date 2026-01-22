package com.poti.android.domain.model.history

import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.PartyStatusType

data class HistoryListContent(
    val ongoingCount: Int,
    val endedCount: Int,
    val items: List<HistoryItem>,
)

data class HistoryItem(
    val id: Long,
    val imageUrl: String?,
    val artist: String,
    val title: String,
    val historyListType: HistoryListType,
    val partyStatus: PartyStatusType,
)
