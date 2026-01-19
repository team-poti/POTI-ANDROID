package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.response.home.HomeResponseDto
import com.poti.android.data.remote.service.HomeService
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val homeService: HomeService,
) {
    suspend fun getHomeContent(): BaseResponse<HomeResponseDto> =
        homeService.getHomeContent()
}
