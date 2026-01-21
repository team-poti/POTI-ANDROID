package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.response.party.GroupBuyPostDto
import com.poti.android.data.remote.dto.response.party.MyRecruitListDto
import com.poti.android.domain.model.history.MyParty
import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.type.HistoryListType
import com.poti.android.domain.type.PartyStatusType

fun MyRecruitListDto.toDomain(): MyPartyList = MyPartyList(
    currentState = try {
        HistoryListType.valueOf(currentStatus)
    } catch (e: IllegalArgumentException) {
        HistoryListType.IN_PROGRESS
    },
    inProgressCount = inProgressCount,
    completedCount = completedCount,
    partyList = groupBuyPosts.map { it.toDomain() },
)

fun GroupBuyPostDto.toDomain(): MyParty = MyParty(
    participationId = null,
    groupBuyId = groupBuyId,
    artistName = artistName,
    productName = productName,
    thumbnailUrl = thumbnailUrl,
    postStatus = try {
        PartyStatusType.valueOf(status)
    } catch (e: IllegalArgumentException) {
        PartyStatusType.COMPLETED
    },
)
