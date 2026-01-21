package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.history.GroupBuyPostParticipantDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupBuyService {
    @GET("/api/v1/posts/{postId}/participants")
    suspend fun getPostParticipant(@Path("postId") postId: Long): BaseResponse<GroupBuyPostParticipantDetailDto>
}
