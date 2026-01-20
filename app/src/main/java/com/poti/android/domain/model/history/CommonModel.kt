package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class PartySummary(
    val imageUrl: String,
    val artist: String,
    val title: String,
    val partyState: ParticipantStatusType,
)

data class ProgressInfo(
    val step: Int,
)
