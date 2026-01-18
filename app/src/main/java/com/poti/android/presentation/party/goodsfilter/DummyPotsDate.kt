package com.poti.android.presentation.party.goodsfilter

import com.poti.android.domain.model.party.PotSummary
import com.poti.android.domain.model.party.Pots

val dummyPots = Pots(
    postTitle = "러브다이브 위드뮤",
    artistName = "IVE(아이브)",
    potSummaries = listOf(
        PotSummary(
            potId = 1L,
            price = "21,300원~",
            goodsImageUrl = "",
            currentCount = 3,
            totalCount = 5,
            availableMembers = "안유진, 장원영",
            profileImageUrl = "",
            nickname = "포티공주",
            rating = "4.8",
        ),
        PotSummary(
            potId = 2L,
            price = "21,300원~",
            goodsImageUrl = "",
            currentCount = 6,
            totalCount = 6,
            availableMembers = "리즈, 레이",
            profileImageUrl = "",
            nickname = "굿즈요정",
            rating = "4.5",
        ),
        PotSummary(
            potId = 3L,
            price = "21,300원~",
            goodsImageUrl = "",
            currentCount = 1,
            totalCount = 4,
            availableMembers = "가을, 이서",
            profileImageUrl = "",
            nickname = "공구마스터",
            rating = "5.0",
        ),
    ),
)
