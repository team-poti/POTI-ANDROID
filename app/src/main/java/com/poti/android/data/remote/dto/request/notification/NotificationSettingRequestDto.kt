package com.poti.android.data.remote.dto.request.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingRequestDto(
    @SerialName("tradeNotificationEnabled")
    val tradeNotificationEnabled: Boolean,
    @SerialName("eventNotificationEnabled")
    val eventNotificationEnabled: Boolean,
)
