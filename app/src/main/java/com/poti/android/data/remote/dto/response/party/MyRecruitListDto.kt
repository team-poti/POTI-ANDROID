package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyRecruitListDto(
    @SerialName("inProgressCount")
    val inProgressCount: Int,
    @SerialName("completedCount")
    val completedCount: Int,
    @SerialName("currentStatus")
    val currentStatus: String,
    @SerialName("groupBuyPosts")
    val groupBuyPosts: List<GroupBuyPostDto>,
)

@Serializable
data class GroupBuyPostDto(
    @SerialName("groupBuyId")
    val groupBuyId: Long,
    @SerialName("artistName")
    val artistName: String,
    @SerialName("productName")
    val productName: String,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("status")
    val status: String,
    @SerialName("createdAt")
    val createdAt: String,
)
