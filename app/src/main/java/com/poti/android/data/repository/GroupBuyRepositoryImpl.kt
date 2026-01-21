package com.poti.android.data.repository

import com.poti.android.core.network.model.handleApiResponse
import com.poti.android.core.network.util.HttpResponseHandler
import com.poti.android.data.mapper.history.toDomain
import com.poti.android.data.remote.service.GroupBuyService
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.repository.GroupBuyRepository
import javax.inject.Inject

class GroupBuyRepositoryImpl @Inject constructor(
    private val httpResponseHandler: HttpResponseHandler,
    private val groupBuyService: GroupBuyService
): GroupBuyRepository {
    override suspend fun getGroupBuyPostParticipant(postId: Long): Result<ParticipantManageDetail> =
        httpResponseHandler.safeApiCall {
            groupBuyService.getPostParticipant(postId)
                .handleApiResponse()
                .getOrThrow()
                .toDomain()
        }

}
