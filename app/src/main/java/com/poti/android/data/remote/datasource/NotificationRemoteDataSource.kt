package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.notification.NotificationSettingRequestDto
import com.poti.android.data.remote.dto.response.notification.NotificationResponseDto
import com.poti.android.data.remote.dto.response.notification.NotificationSettingResponseDto
import com.poti.android.data.remote.service.NotificationService
import javax.inject.Inject

class NotificationRemoteDataSource @Inject constructor(
    private val notificationService: NotificationService,
) {
    suspend fun getNotificationList(
        page: Int?,
        size: Int?,
        sort: List<String>?,
    ): BaseResponse<NotificationResponseDto> =
        notificationService.getNotificationList(
            page = page,
            size = size,
            sort = sort,
        )

    suspend fun getNotificationSetting(): BaseResponse<NotificationSettingResponseDto> =
        notificationService.getNotificationSetting()

    suspend fun patchNotificationSetting(
        notificationSettingRequest: NotificationSettingRequestDto,
    ): BaseResponse<NotificationSettingResponseDto> =
        notificationService.patchNotificationSetting(body = notificationSettingRequest)
}
