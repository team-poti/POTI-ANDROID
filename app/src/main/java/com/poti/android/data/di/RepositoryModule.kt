package com.poti.android.data.di

import com.poti.android.data.repository.ArtistRepositoryImpl
import com.poti.android.data.repository.AuthRepositoryImpl
import com.poti.android.data.repository.DeliveryRepositoryImpl
import com.poti.android.data.repository.GroupBuyRepositoryImpl
import com.poti.android.data.repository.HomeRepositoryImpl
import com.poti.android.data.repository.ImageRepositoryImpl
import com.poti.android.data.repository.PartyRepositoryImpl
import com.poti.android.data.repository.PaymentRepositoryImpl
import com.poti.android.data.repository.UserRepositoryImpl
import com.poti.android.domain.repository.ArtistRepository
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.DeliveryRepository
import com.poti.android.domain.repository.GroupBuyRepository
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.domain.repository.ImageRepository
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.domain.repository.PaymentRepository
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

    @Binds
    @Singleton
    abstract fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository

    @Binds
    @Singleton
    abstract fun bindPartyRepository(partyRepositoryImpl: PartyRepositoryImpl): PartyRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindGroupBuyRepository(groupBuyRepositoryImpl: GroupBuyRepositoryImpl): GroupBuyRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindDeliveryRepository(deliveryRepositoryImpl: DeliveryRepositoryImpl): DeliveryRepository
}
