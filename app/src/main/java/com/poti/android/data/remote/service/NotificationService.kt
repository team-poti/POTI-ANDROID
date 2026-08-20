package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.notification.NotificationSettingRequestDto
import com.poti.android.data.remote.dto.response.notification.NotificationResponseDto
import com.poti.android.data.remote.dto.response.notification.NotificationSettingResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationService {
    @GET("/api/v1/notifications")
    suspend fun getNotificationList(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("sort") sort: List<String>?,
    ): BaseResponse<NotificationResponseDto>

    @GET("/api/v1/notifications/settings")
    suspend fun getNotificationSetting(): BaseResponse<NotificationSettingResponseDto>

    @PATCH("/api/v1/notifications/settings")
    suspend fun patchNotificationSetting(
        @Body body: NotificationSettingRequestDto,
    ): BaseResponse<NotificationSettingResponseDto>
}
