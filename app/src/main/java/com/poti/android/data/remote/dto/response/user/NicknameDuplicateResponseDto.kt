package com.poti.android.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameDuplicateResponseDto(
    @SerialName("isDuplicated")
    val isDuplicated: Boolean,
)
