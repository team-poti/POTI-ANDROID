package com.poti.android.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameDuplicateRequestDto(
    @SerialName("nickname")
    val nickname: String,
)
