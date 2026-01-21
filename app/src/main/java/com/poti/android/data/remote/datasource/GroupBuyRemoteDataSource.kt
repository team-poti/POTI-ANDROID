package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.GroupBuyPostParticipantDetailDto
import com.poti.android.data.remote.service.GroupBuyService
import javax.inject.Inject

class GroupBuyRemoteDataSource @Inject constructor(
    private val groupBuyService: GroupBuyService
) {
    suspend fun getPostParticipant(postId: Long): BaseResponse<GroupBuyPostParticipantDetailDto> =
        groupBuyService.getPostParticipant(postId)
}
