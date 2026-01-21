package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.response.party.PartyDto
import com.poti.android.data.remote.dto.response.party.PartyListResponseDto
import com.poti.android.domain.model.party.PartyList
import com.poti.android.domain.model.party.PartySummary

fun PartyListResponseDto.toDomain(): PartyList =
    PartyList(
        partyTitle = postTitle,
        artistName = artist,
        partySummaries = pots.map { it.toDomain() },
    )

private fun PartyDto.toDomain(): PartySummary =
    PartySummary(
        partyId = potId,
        price = price,
        goodsImageUrl = thumbnailUrl.orEmpty(),
        currentCount = currentCount,
        totalCount = totalCount,
        availableMembers = availableMembers,
        profileImageUrl = uploader.profileImage,
        nickname = uploader.nickname,
        rating = uploader.rating,
    )
