package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePartyResponseDto(
    @SerialName("postId")
    val postId: Long,
)
