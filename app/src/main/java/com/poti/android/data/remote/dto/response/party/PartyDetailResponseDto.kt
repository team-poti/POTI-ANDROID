package com.poti.android.data.remote.dto.response.party

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartyDetailResponseDto(
    @SerialName("postId")
    val postId: Long,
    @SerialName("isMyPost")
    val isMyPost: Boolean,
    @SerialName("isParticipated")
    val isParticipated: Boolean = false,
    @SerialName("status")
    val status: String,
    @SerialName("artist")
    val artist: String,
    @SerialName("artistId")
    val artistId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: Int,
    @SerialName("uploadTime")
    val uploadTime: String,
    @SerialName("deadline")
    val deadline: String,
    @SerialName("images")
    val images: List<ImageResponseDto>,
    @SerialName("content")
    val content: String,
    @SerialName("shippingOptions")
    val shippingOptionDtos: List<ShippingOptionDto>,
    @SerialName("uploader")
    val uploaderDto: UploaderDto,
    @SerialName("currentCount")
    val currentCount: Int,
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("participants")
    val participants: List<ParticipantDto>,
)

@Serializable
data class ImageResponseDto(
    @SerialName("order")
    val order: Int,
    @SerialName("url")
    val url: String,
)

@Serializable
data class ShippingOptionDto(
    @SerialName("shippingId")
    val shippingId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)

@Serializable
data class UploaderDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String?,
    @SerialName("rating")
    val rating: Double,
    @SerialName("reviewCount")
    val reviewCount: Int,
)

@Serializable
data class ParticipantDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String?,
    @SerialName("rating")
    val rating: Double,
    @SerialName("selectedMembers")
    val selectedMembers: List<String>,
)
