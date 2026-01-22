package com.poti.android.data.remote.dto.response.history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecruiterDetailDto(
    @SerialName("postId")
    val postId: Long,
    @SerialName("orderNumber")
    val orderNumber: String,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("title")
    val title: String,
    @SerialName("postStatus")
    val postStatus: String,
    @SerialName("statusMessage")
    val statusMessage: String,
    @SerialName("participant")
    val participant: List<ParticipantDto>,
)
