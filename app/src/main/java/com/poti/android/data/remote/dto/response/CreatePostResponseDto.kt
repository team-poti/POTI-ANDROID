package com.poti.android.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostResponseDto(
    @SerialName("postId")
    val postId: Long,
)
