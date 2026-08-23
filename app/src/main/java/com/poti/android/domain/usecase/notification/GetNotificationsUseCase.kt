package com.poti.android.domain.usecase.notification

import com.poti.android.domain.model.notification.NotificationList
import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        page: Int = 0,
        size: Int = 20,
    ): Result<NotificationList> = notificationRepository.getNotifications(page, size)
}
