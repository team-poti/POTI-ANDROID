package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.home.HomeResponseDto
import retrofit2.http.GET

interface HomeService {
    @GET("/api/v1/home")
    suspend fun getHomeContent(): BaseResponse<HomeResponseDto>
}
