package com.poti.android.data.mapper.artist

import com.poti.android.data.remote.dto.request.post.OptionRequestDto
import com.poti.android.data.remote.dto.response.artist.MemberListResponseDto
import com.poti.android.domain.model.artist.MemberPriceOption

fun MemberListResponseDto.toPriceDomain(): List<MemberPriceOption> = members.map {
    MemberPriceOption(
        memberId = it.memberId,
        name = it.name,
        price = ""
    )
}

fun MemberPriceOption.toDto(): OptionRequestDto =
    OptionRequestDto(
        memberId = this.memberId,
        price = this.price.toIntOrNull() ?: 0,
    )
