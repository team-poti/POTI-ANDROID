package com.poti.android.domain.repository

import com.poti.android.domain.model.history.MyPartyList
import com.poti.android.domain.model.history.ParticipantDetail

interface ParticipationRepository {
    suspend fun getMyParticipationList(status: String): Result<MyPartyList>
    suspend fun getParticipantDetail(participationId: Long): Result<ParticipantDetail>
}
