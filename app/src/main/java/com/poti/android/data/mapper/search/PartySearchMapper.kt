package com.poti.android.data.mapper.search

import com.poti.android.data.remote.dto.response.search.PartySearchItemResponseDto
import com.poti.android.data.remote.dto.response.search.PartySearchResponseDto
import com.poti.android.domain.model.search.PartySearchItem
import com.poti.android.domain.model.search.PartySearchResult

fun PartySearchResponseDto.toDomain(): PartySearchResult =
    PartySearchResult(
        items = content.map { it.toDomain() },
        hasNext = false,
    )

private fun PartySearchItemResponseDto.toDomain(): PartySearchItem =
    PartySearchItem(
        artist = artist,
        artistId = artistId,
        postImage = postImage.orEmpty(),
        postTitle = postTitle,
        postCount = postCount.toInt(),
        tag = tag,
    )
