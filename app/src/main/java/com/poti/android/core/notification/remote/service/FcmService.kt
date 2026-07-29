package com.poti.android.core.notification.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.core.notification.remote.dto.request.FcmTokenRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmService {
    @DELETE("/api/v1/fcm-tokens")
    suspend fun deleteFcmToken(
        @Query("token") token: String,
    ): BaseResponse<Unit>

    @POST("/api/v1/fcm-tokens")
    suspend fun postFcmToken(
        @Body request: FcmTokenRequestDto,
    ): BaseResponse<Unit>
}
