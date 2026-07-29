package com.poti.android.core.notification.di

import com.poti.android.core.notification.repository.FcmRepository
import com.poti.android.core.notification.repository.FcmRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FcmRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFcmRepository(fcmRepositoryImpl: FcmRepositoryImpl): FcmRepository
}
