package com.poti.android.core.notification.di

import com.poti.android.core.notification.remote.service.FcmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FcmServiceModule {
    @Provides
    @Singleton
    fun provideFcmService(retrofit: Retrofit): FcmService =
        retrofit.create(FcmService::class.java)
}
