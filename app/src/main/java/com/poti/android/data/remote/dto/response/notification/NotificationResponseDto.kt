package com.poti.android.data.remote.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    @SerialName("content")
    val content: List<NotificationItemResponseDto>,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class NotificationItemResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("body")
    val body: String,
    @SerialName("type")
    val type: String,
    @SerialName("deeplink")
    val deeplink: String?,
    @SerialName("read")
    val read: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
)
