package com.poti.android.data.mapper.participation

import com.poti.android.data.remote.dto.response.participant.MyPartyListDto
import com.poti.android.data.remote.dto.response.participant.ParticipantDto
import com.poti.android.domain.model.history.MyParty
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.PartyStatusType

fun MyPartyListDto.toDomain(): MyPartyList = MyPartyList(
    currentState = try {
        HistoryListType.valueOf(currentStatus)
    } catch (e: IllegalArgumentException) {
        HistoryListType.IN_PROGRESS
    },
    inProgressCount = inProgressCount,
    completedCount = completedCount,
    partyList = participations.map { it.toDomain() },
)

fun ParticipantDto.toDomain(): MyParty = MyParty(
    participationId = participationId,
    groupBuyId = groupBuyId,
    artistName = artistName,
    productName = productName,
    thumbnailUrl = thumbnailUrl,
    postStatus = try {
        PartyStatusType.valueOf(postStatus)
    } catch (e: IllegalArgumentException) {
        PartyStatusType.COMPLETED
    },
)
