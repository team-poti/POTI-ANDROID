package com.poti.android.domain.repository

import com.poti.android.domain.model.history.ParticipantManageDetail

interface GroupBuyRepository {
    suspend fun getGroupBuyPostParticipant(postId: Long): Result<ParticipantManageDetail>
}
