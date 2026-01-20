package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.post.PostDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PostService {
    @GET("/api/v1/posts/{postId}")
    suspend fun getPartyDetail(
        @Path("postId") postId: Long,
    ): BaseResponse<PostDetailResponseDto>
}
