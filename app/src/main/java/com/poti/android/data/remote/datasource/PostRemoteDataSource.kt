package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.post.PostDetailResponseDto
import com.poti.android.data.remote.service.PostService
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val postService: PostService,
) {
    suspend fun getPostDetail(postId: Long): BaseResponse<PostDetailResponseDto> =
        postService.getPartyDetail(postId = postId)
}
