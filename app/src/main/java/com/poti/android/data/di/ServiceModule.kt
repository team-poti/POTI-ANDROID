package com.poti.android.data.di

import com.poti.android.data.remote.service.ArtistService
import com.poti.android.data.remote.service.AuthService
import com.poti.android.data.remote.service.DeliveryService
import com.poti.android.data.remote.service.HomeService
import com.poti.android.data.remote.service.ImageService
import com.poti.android.data.remote.service.ParticipationService
import com.poti.android.data.remote.service.PartyService
import com.poti.android.data.remote.service.PaymentService
import com.poti.android.data.remote.service.ReviewService
import com.poti.android.data.remote.service.SearchService
import com.poti.android.data.remote.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideImageService(retrofit: Retrofit): ImageService =
        retrofit.create(ImageService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @ReissueClient
    @Provides
    @Singleton
    fun provideReissueAuthService(
        @ReissueClient retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideArtistService(retrofit: Retrofit): ArtistService =
        retrofit.create(ArtistService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun providePartyService(retrofit: Retrofit): PartyService =
        retrofit.create(PartyService::class.java)

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)

    @Provides
    @Singleton
    fun provideParticipationService(retrofit: Retrofit): ParticipationService =
        retrofit.create(ParticipationService::class.java)

    @Provides
    @Singleton
    fun providePaymentService(retrofit: Retrofit): PaymentService {
        return retrofit.create(PaymentService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeliveryService(retrofit: Retrofit): DeliveryService {
        return retrofit.create(DeliveryService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewService(retrofit: Retrofit): ReviewService =
        retrofit.create(ReviewService::class.java)
}
