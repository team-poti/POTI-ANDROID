package com.poti.android.domain.repository

import com.poti.android.domain.model.home.HomeContent

interface HomeRepository {
    suspend fun getHomeContent(): Result<HomeContent>
}
