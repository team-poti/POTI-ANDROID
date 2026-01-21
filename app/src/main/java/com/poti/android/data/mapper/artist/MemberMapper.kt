package com.poti.android.data.mapper.artist

import com.poti.android.data.remote.dto.response.artist.MemberListResponseDto
import com.poti.android.domain.model.artist.Member

fun MemberListResponseDto.toDomain(): List<Member> = members.map {
    Member(
        memberId = it.memberId,
        name = it.name,
    )
}
