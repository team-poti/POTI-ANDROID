package com.poti.android.data.di

import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.data.auth.DefaultSocialLoginLauncher
import com.poti.android.data.auth.KakaoAuthClient
import com.poti.android.data.auth.KakaoSdkAuthClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocialAuthModule {
    @Binds
    abstract fun bindKakaoAuthClient(
        client: KakaoSdkAuthClient,
    ): KakaoAuthClient

    @Binds
    @Singleton
    abstract fun bindSocialLoginLauncher(
        launcher: DefaultSocialLoginLauncher,
    ): SocialLoginLauncher
}
