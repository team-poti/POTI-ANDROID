package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.participant.MyPartyListDto
import com.poti.android.data.remote.service.ParticipationService
import javax.inject.Inject

class ParticipantRemoteDataSource @Inject constructor(
    private val participationService: ParticipationService,
) {
    suspend fun getMyPartyList(status: String): BaseResponse<MyPartyListDto> =
        participationService.getMyParticipationList(status)
}
