package com.poti.android.domain.repository

import com.poti.android.domain.model.history.MyPartyList

interface ParticipationRepository {
    suspend fun getMyParticipationList(status: String): Result<MyPartyList>
}
