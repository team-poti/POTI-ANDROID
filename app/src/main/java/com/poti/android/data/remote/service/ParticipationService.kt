package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.MyPartyListDto
import com.poti.android.data.remote.dto.response.history.ParticipantDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParticipationService {
    @GET("/api/v1/participations")
    suspend fun getMyParticipationList(
        @Query("status") status: String,
    ): BaseResponse<MyPartyListDto>

    @GET("/api/v1/participations/{participationId}")
    suspend fun getParticipantDetail(
        @Path("participationId") participantId: Long
    ): BaseResponse<ParticipantDetailResponseDto>
}
