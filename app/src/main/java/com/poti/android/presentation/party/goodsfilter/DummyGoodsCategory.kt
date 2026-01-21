package com.poti.android.presentation.party.goodsfilter

import com.poti.android.domain.model.party.GoodsCategory
import com.poti.android.domain.model.party.GroupItem

val dummyGoodsCategory = GoodsCategory(
    nickname = "포티",
    mainArtist = "아이브",
    mainArtistId = 1L,
    groupItems = listOf(
        GroupItem(
            postTitle = "아이브 공식 응원봉",
            artist = "IVE",
            artistId = 1L,
            postImage = "",
            postCount = 3,
            tag = "인기",
        ),
        GroupItem(
            postTitle = "아이브 콘서트 MD 세트",
            artist = "IVE",
            artistId = 1L,
            postImage = "",
            postCount = 5,
            tag = "NEW",
        ),
        GroupItem(
            postTitle = "아이브 포토카드 랜덤팩",
            artist = "IVE",
            artistId = 1L,
            postImage = "",
            postCount = 2,
            tag = null,
        ),
        GroupItem(
            postTitle = "아이브 시즌그리팅",
            artist = "IVE",
            artistId = 1L,
            postImage = "",
            postCount = 7,
            tag = "마감임박",
        ),
    ),
    myGroupItems = emptyList(),
)
