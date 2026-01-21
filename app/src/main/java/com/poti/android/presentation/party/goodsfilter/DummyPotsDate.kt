package com.poti.android.presentation.party.goodsfilter

import com.poti.android.domain.model.party.PartyList
import com.poti.android.domain.model.party.PartySummary

val dummyPartyList = PartyList(
    partyTitle = "러브다이브 위드뮤",
    artistName = "IVE(아이브)",
    partySummaries = listOf(
        PartySummary(
            partyId = 1L,
            price = 21300,
            goodsImageUrl = "",
            currentCount = 3,
            totalCount = 5,
            availableMembers = listOf("원영", "유진"),
            profileImageUrl = "",
            nickname = "포티공주",
            rating = 4.8,
        ),
        PartySummary(
            partyId = 2L,
            price = 21300,
            goodsImageUrl = "",
            currentCount = 6,
            totalCount = 6,
            availableMembers = listOf("원영", "유진"),
            profileImageUrl = "",
            nickname = "굿즈요정",
            rating = 4.5,
        ),
        PartySummary(
            partyId = 3L,
            price = 21300,
            goodsImageUrl = "",
            currentCount = 1,
            totalCount = 4,
            availableMembers = listOf("원영", "유진"),
            profileImageUrl = "",
            nickname = "공구마스터",
            rating = 5.0,
        ),
    ),
)
