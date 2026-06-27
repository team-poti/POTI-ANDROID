package com.poti.android.data.mapper.party

import com.poti.android.data.remote.dto.response.party.PartyDto
import com.poti.android.data.remote.dto.response.party.ProductPartyListResponseDto
import com.poti.android.domain.model.party.PartySummary
import com.poti.android.domain.model.party.ProductPartyList

fun ProductPartyListResponseDto.toDomain(): ProductPartyList =
    ProductPartyList(
        partyTitle = postTitle,
        artistName = artist,
        partySummaries = pots.map { it.toDomain() },
        currentPage = currentPage,
        hasNext = hasNext,
    )

private fun PartyDto.toDomain(): PartySummary =
    PartySummary(
        partyId = potId,
        price = price,
        productImageUrl = thumbnailUrl.orEmpty(),
        currentCount = currentCount,
        totalCount = totalCount,
        availableMembers = availableMembers,
        profileImageUrl = uploader.profileImage,
        nickname = uploader.nickname,
        rating = uploader.rating,
    )
