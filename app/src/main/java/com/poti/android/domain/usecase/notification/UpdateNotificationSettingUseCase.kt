package com.poti.android.domain.usecase.notification

import com.poti.android.domain.model.notification.NotificationSetting
import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationSettingUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke(
        isTradeEnabled: Boolean,
        isEventEnabled: Boolean,
    ): Result<NotificationSetting> =
        notificationRepository.updateNotificationSetting(isTradeEnabled, isEventEnabled)
}
