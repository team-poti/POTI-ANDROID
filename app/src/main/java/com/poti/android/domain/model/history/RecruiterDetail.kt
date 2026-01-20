package com.poti.android.domain.model.history

import com.poti.android.domain.type.ParticipantStatusType

data class RecruiterDetail(
    val partyId: Long,
    val artistInfo: ArtistInfo,
    val progressInfo: ProgressInfo,
    val participantInfoList: List<ParticipantInfo>,
    val participantCount: Int = participantInfoList.size,
)

data class ArtistInfo(
    val imageUrl: String,
    val artist: String,
    val title: String,
    val partyState: ParticipantStatusType,
)

data class ProgressInfo(
    val guideText: String,
    val step: Int,
)

data class ParticipantInfo(
    val userId: Long,
    val memberNames: String,
    val participantState: ParticipantStatusType,
    val userInfo: String,
    val deliveryMethod: String,
    val deliveryPrice: Int,
)
