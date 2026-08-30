package com.poti.android.data.mapper.notification

import com.poti.android.data.remote.dto.response.notification.NotificationItemResponseDto
import com.poti.android.data.remote.dto.response.notification.NotificationResponseDto
import com.poti.android.domain.model.notification.Notification
import com.poti.android.domain.model.notification.NotificationList
import com.poti.android.domain.type.NotificationType

fun NotificationResponseDto.toDomain(): NotificationList =
    NotificationList(
        notifications = content.map { it.toDomain() },
        hasNext = hasNext,
    )

private fun NotificationItemResponseDto.toDomain(): Notification =
    Notification(
        id = id,
        title = title,
        body = body,
        type = runCatching { NotificationType.valueOf(type) }.getOrDefault(NotificationType.EVENT),
        deepLink = deeplink.orEmpty(),
        isRead = read,
        createdAt = createdAt,
    )
