package com.poti.android.presentation.party.detail

import com.poti.android.domain.model.Participant
import com.poti.android.domain.model.PartyDetail
import com.poti.android.domain.model.PartyImage
import com.poti.android.domain.model.ShippingOption
import com.poti.android.domain.model.Uploader
import com.poti.android.domain.type.PartyStatusType

val dummyPartyDetail = PartyDetail(
    postId = 1,
    isMyPost = false,
    isLiked = false,
    status = PartyStatusType.RECRUITING,
    artist = "IVE(아이브)",
    title = "러브다이브 위드뮤",
    price = 5000,
    uploadTime = "4시간 전",
    deadline = "2025-12-31",
    content = "내용내용내용\n내용내용내용",
    images = listOf(
        PartyImage(
            order = 1,
            url = "https://i.pinimg.com/736x/ad/6f/c0/ad6fc0da5a240a59524a64f0d168659f.jpg",
        ),
        PartyImage(
            order = 2,
            url = "https://i.pinimg.com/736x/54/a1/f6/54a1f60741b33e99d574e81ccf4f5b9e.jpg",
        ),
        PartyImage(
            order = 3,
            url = "https://i.pinimg.com/1200x/61/73/3b/61733b2ae4023c9826ec7d303dab0ba0.jpg",
        ),
    ),
    shippingOptions = listOf(
        ShippingOption(
            name = "택배",
            price = 4000,
        ),
        ShippingOption(
            name = "준등기",
            price = 1800,
        ),
    ),
    uploader = Uploader(
        userId = 1,
        nickname = "닉네임",
        profileImage = null,
        rating = 4.8,
        reviewCount = 14,
    ),
    participants = listOf(
        Participant(
            userId = 1,
            nickname = "참여자1",
            profileImage = null,
            rating = 4.5,
            selectedMembers = listOf("원영"),
        ),
        Participant(
            userId = 1,
            nickname = "참여자1",
            profileImage = null,
            rating = 4.5,
            selectedMembers = listOf("이서"),
        ),
        Participant(
            userId = 2,
            nickname = "참여자2",
            profileImage = null,
            rating = 3.4,
            selectedMembers = listOf("유진"),
        ),
    ),
    currentCount = 2,
    totalCount = 5,
)
