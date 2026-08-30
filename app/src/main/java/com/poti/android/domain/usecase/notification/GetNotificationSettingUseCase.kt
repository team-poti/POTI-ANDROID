package com.poti.android.domain.usecase.notification

import com.poti.android.domain.model.notification.NotificationSetting
import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationSettingUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<NotificationSetting> =
        notificationRepository.getNotificationSetting()
}
