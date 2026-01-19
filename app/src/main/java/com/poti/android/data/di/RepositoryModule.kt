package com.poti.android.data.di

import com.poti.android.data.repository.ArtistRepositoryImpl
import com.poti.android.data.repository.AuthRepositoryImpl
import com.poti.android.data.repository.UserRepositoryImpl
import com.poti.android.domain.repository.ArtistRepository
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository
}
