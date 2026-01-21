package com.poti.android.data.remote.dto.response.participant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyPartyListDto(
    @SerialName("currentStatus")
    val currentStatus: String,
    @SerialName("inProgressCount")
    val inProgressCount: Int,
    @SerialName("completedCount")
    val completedCount: Int,
    @SerialName("participations")
    val participations: List<ParticipantDto>,
)

@Serializable
data class ParticipantDto(
    @SerialName("participationId")
    val participationId: Long,
    @SerialName("groupBuyId")
    val groupBuyId: Long,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("productName")
    val productName: String,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("postStatus")
    val postStatus: String,
)
