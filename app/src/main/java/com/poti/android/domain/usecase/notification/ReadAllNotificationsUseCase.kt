package com.poti.android.domain.usecase.notification

import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class ReadAllNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<Unit> = notificationRepository.readAllNotifications()
}
