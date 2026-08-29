package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.model.handleNullableApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.notification.toDomain
import com.poti.android.data.mock.UiMockData
import com.poti.android.data.mock.executeWithUiMock
import com.poti.android.data.remote.datasource.NotificationRemoteDataSource
import com.poti.android.data.remote.dto.request.notification.NotificationSettingRequestDto
import com.poti.android.domain.model.notification.NotificationList
import com.poti.android.domain.model.notification.NotificationSetting
import com.poti.android.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {
    override suspend fun getNotifications(
        page: Int,
        size: Int,
    ): Result<NotificationList> = executeWithUiMock(
        mock = {
            val notifications = UiMockData.notifications

            NotificationList(
                notifications = notifications
                    .drop(page * size)
                    .take(size),
                hasNext = (page + 1) * size < notifications.size,
            )
        },
        real = {
            httpResponseHandler.safeApiCall {
                notificationRemoteDataSource.getNotificationList(
                    page = page,
                    size = size,
                    sort = null,
                )
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun getNotificationSetting(): Result<NotificationSetting> = executeWithUiMock(
        mock = { UiMockData.notificationSetting },
        real = {
            httpResponseHandler.safeApiCall {
                notificationRemoteDataSource.getNotificationSetting()
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun updateNotificationSetting(
        isTradeEnabled: Boolean,
        isEventEnabled: Boolean,
    ): Result<NotificationSetting> = executeWithUiMock(
        mock = {
            NotificationSetting(
                isTradeEnabled = isTradeEnabled,
                isEventEnabled = isEventEnabled,
            )
        },
        real = {
            httpResponseHandler.safeApiCall {
                val requestDto = NotificationSettingRequestDto(
                    tradeNotificationEnabled = isTradeEnabled,
                    eventNotificationEnabled = isEventEnabled,
                )
                notificationRemoteDataSource
                    .patchNotificationSetting(notificationSettingRequest = requestDto)
                    .handleApiResponse()
                    .getOrThrow()
                    .toDomain()
            }
        },
    )

    override suspend fun readNotification(notificationId: Long): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                notificationRemoteDataSource
                    .patchNotificationRead(notificationId = notificationId)
                    .handleNullableApiResponse()
                    .getOrThrow()
                Unit
            }
        },
    )

    override suspend fun readAllNotifications(): Result<Unit> = executeWithUiMock(
        mock = { Unit },
        real = {
            httpResponseHandler.safeApiCall {
                notificationRemoteDataSource
                    .patchNotificationReadAll()
                    .handleNullableApiResponse()
                    .getOrThrow()
                Unit
            }
        },
    )
}
