package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.MyPartyListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ParticipationService {
    @GET("/api/v1/participations")
    suspend fun getMyParticipationList(
        @Query("status") status: String,
    ): BaseResponse<MyPartyListDto>
}
