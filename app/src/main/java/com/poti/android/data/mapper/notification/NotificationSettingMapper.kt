package com.poti.android.data.mapper.notification

import com.poti.android.data.remote.dto.response.notification.NotificationSettingResponseDto
import com.poti.android.domain.model.notification.NotificationSetting

fun NotificationSettingResponseDto.toDomain(): NotificationSetting =
    NotificationSetting(
        isTradeEnabled = tradeNotificationEnabled,
        isEventEnabled = eventNotificationEnabled,
    )
