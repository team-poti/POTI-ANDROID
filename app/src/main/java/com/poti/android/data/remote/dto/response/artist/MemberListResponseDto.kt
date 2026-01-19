package com.poti.android.data.remote.dto.response.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberListResponseDto (
    @SerialName("members")
    val members: List<MemberResponseDto>
)

@Serializable
data class MemberResponseDto(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("name")
    val name: String,
)
