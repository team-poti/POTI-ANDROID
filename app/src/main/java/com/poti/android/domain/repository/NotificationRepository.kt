package com.poti.android.domain.repository

import com.poti.android.domain.model.notification.NotificationList
import com.poti.android.domain.model.notification.NotificationSetting

interface NotificationRepository {
    suspend fun getNotifications(
        page: Int = 0,
        size: Int = 20,
    ): Result<NotificationList>

    suspend fun getNotificationSetting(): Result<NotificationSetting>

    suspend fun updateNotificationSetting(
        isTradeEnabled: Boolean,
        isEventEnabled: Boolean,
    ): Result<NotificationSetting>
}
