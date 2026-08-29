package com.poti.android.domain.model.notification

data class NotificationList(
    val notifications: List<Notification>,
    val hasNext: Boolean,
)
