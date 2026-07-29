package com.poti.android.core.notification.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.core.notification.remote.dto.request.FcmTokenRequestDto
import com.poti.android.core.notification.remote.service.FcmService
import javax.inject.Inject

class FcmRemoteDataSource @Inject constructor(
    private val fcmService: FcmService,
) {
    suspend fun deleteFcmToken(token: String): BaseResponse<Unit> =
        fcmService.deleteFcmToken(token)

    suspend fun postFcmToken(request: FcmTokenRequestDto): BaseResponse<Unit> =
        fcmService.postFcmToken(request)
}
