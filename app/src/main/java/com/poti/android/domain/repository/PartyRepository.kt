package com.poti.android.domain.repository

import com.poti.android.domain.model.party.PartyDetail

interface PartyRepository {
    suspend fun getPartyDetail(partyId: Long): Result<PartyDetail>
}
