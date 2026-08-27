package com.poti.android.domain.model.notification

import com.poti.android.domain.type.NotificationType

data class Notification(
    val id: Long,
    val title: String,
    val body: String,
    val type: NotificationType,
    val deepLink: String,
    val isRead: Boolean,
    val createdAt: String,
)
