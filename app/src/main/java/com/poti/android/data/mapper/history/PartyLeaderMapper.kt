package com.poti.android.data.mapper.history

import com.poti.android.data.remote.dto.response.history.DeliveryConfirmResponseDto
import com.poti.android.domain.model.history.PartyLeader

fun DeliveryConfirmResponseDto.toDomain(): PartyLeader = PartyLeader(
    leaderUserId = this.leaderUserId,
)
