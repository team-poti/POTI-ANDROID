package com.poti.android.domain.repository

import com.poti.android.domain.model.home.HomeContent
import com.poti.android.domain.model.party.GoodsCategory

interface HomeRepository {
    suspend fun getHomeContent(): Result<HomeContent>

    suspend fun getGoodsCategoryList(
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
        artistId: Long? = null,
    ): Result<GoodsCategory>
}
