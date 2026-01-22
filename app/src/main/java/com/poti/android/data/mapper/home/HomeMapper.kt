package com.poti.android.data.mapper.home

import com.poti.android.data.remote.dto.response.home.HomeBannerDto
import com.poti.android.data.remote.dto.response.home.HomeResponseDto
import com.poti.android.data.remote.dto.response.home.MyGroupItemDto
import com.poti.android.data.remote.dto.response.home.OtherGroupItemDto
import com.poti.android.domain.model.home.Banner
import com.poti.android.domain.model.home.GroupItem
import com.poti.android.domain.model.home.HomeContent

fun HomeResponseDto.toDomain(): HomeContent = HomeContent(
    nickname = nickname,
    mainArtist = mainArtist,
    mainArtistId = mainArtistId,
    myGroupItems = myGroupItems.orEmpty().map { it.toDomain() },
    otherGroupItems = otherGroupItems.map { it.toDomain() },
    banners = banners.map { it.toDomain() },
)

fun MyGroupItemDto.toDomain(): GroupItem = GroupItem(
    postTitle = postTitle.orEmpty(),
    artist = artist.orEmpty(),
    artistId = artistId ?: 0,
    postImage = postImage.orEmpty(),
    postCount = postCount ?: 0,
    tag = tag.orEmpty(),
)

fun OtherGroupItemDto.toDomain(): GroupItem = GroupItem(
    postTitle = postTitle,
    artist = artist,
    artistId = artistId ?: 0,
    postImage = postImage.orEmpty(),
    postCount = postCount,
    tag = tag,
)

fun HomeBannerDto.toDomain(): Banner = Banner(
    postId = postId,
    imageUrl = imageUrl,
)
