package com.poti.android.data.remote.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingResponseDto(
    @SerialName("tradeNotificationEnabled")
    val tradeNotificationEnabled: Boolean,
    @SerialName("eventNotificationEnabled")
    val eventNotificationEnabled: Boolean,
)
