package com.poti.android.data.mapper.home

import com.poti.android.data.remote.dto.response.home.GoodsCategoryResponseDto
import com.poti.android.data.remote.dto.response.home.GroupItemDto
import com.poti.android.domain.model.party.GoodsCategory
import com.poti.android.domain.model.party.GroupItem

fun GoodsCategoryResponseDto.toDomain(): GoodsCategory =
    GoodsCategory(
        nickname = nickname,
        mainArtist = mainArtist,
        mainArtistId = mainArtistId,
        groupItems = groupItems?.map { it.toDomain() }.orEmpty(),
        myGroupItems = myGroupItems?.map { it.toDomain() }.orEmpty(),
    )

fun GroupItemDto.toDomain(): GroupItem =
    GroupItem(
        artist = artist.orEmpty(),
        artistId = artistId ?: 0L,
        postImage = postImage.orEmpty(),
        postTitle = postTitle.orEmpty(),
        postCount = postCount ?: 0,
        tag = tag,
    )
