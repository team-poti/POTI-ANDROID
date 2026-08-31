package com.poti.android.domain.usecase.notification

import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        notificationId: Long,
    ): Result<Unit> = notificationRepository.readNotification(notificationId)
}
