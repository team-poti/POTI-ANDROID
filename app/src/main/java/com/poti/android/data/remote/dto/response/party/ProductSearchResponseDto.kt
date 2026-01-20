package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductSearchResponseDto(
    @SerialName("titles")
    val titles: List<String>,
)
