package com.poti.android.data.di

import com.poti.android.data.repository.ArtistRepositoryImpl
import com.poti.android.data.repository.AuthRepositoryImpl
import com.poti.android.data.repository.DeliveryRepositoryImpl
import com.poti.android.data.repository.HomeRepositoryImpl
import com.poti.android.data.repository.ImageRepositoryImpl
import com.poti.android.data.repository.ParticipationRepositoryImpl
import com.poti.android.data.repository.PartyRepositoryImpl
import com.poti.android.data.repository.PaymentRepositoryImpl
import com.poti.android.data.repository.ReviewRepositoryImpl
import com.poti.android.data.repository.S3RepositoryImpl
import com.poti.android.data.repository.UserRepositoryImpl
import com.poti.android.domain.repository.ArtistRepository
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.DeliveryRepository
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.domain.repository.ImageRepository
import com.poti.android.domain.repository.ParticipationRepository
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.domain.repository.PaymentRepository
import com.poti.android.domain.repository.ReviewRepository
import com.poti.android.domain.repository.S3Repository
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
    abstract fun bindParticipationRepository(participationRepositoryImpl: ParticipationRepositoryImpl): ParticipationRepository

    @Binds
    @Singleton
    abstract fun bindS3Repository(s3RepositoryImpl: S3RepositoryImpl): S3Repository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindDeliveryRepository(deliveryRepositoryImpl: DeliveryRepositoryImpl): DeliveryRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository
}
