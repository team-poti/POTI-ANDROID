package com.poti.android.data.remote.service

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.home.GoodsCategoryResponseDto
import com.poti.android.data.remote.dto.response.home.HomeResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {
    @GET("/api/v1/home")
    suspend fun getHomeContent(): BaseResponse<HomeResponseDto>

    @GET("/api/v1/feeds")
    suspend fun getGoodsCategoryList(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("sort") sort: String?,
        @Query("artistId") artistId: Long?,
    ): BaseResponse<GoodsCategoryResponseDto>
}
