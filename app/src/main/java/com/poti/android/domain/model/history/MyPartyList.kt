package com.poti.android.domain.model.history

import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.PartyStatusType

data class MyPartyList(
    val currentState: HistoryListType,
    val inProgressCount: Int,
    val completedCount: Int,
    val partyList: List<MyParty>,
)

data class MyParty(
    val participationId: Long?,
    val groupBuyId: Long,
    val artistName: String,
    val productName: String,
    val thumbnailUrl: String?,
    val postStatus: PartyStatusType,
) {
    val uiId: Long
        get() = participationId ?: groupBuyId
}
