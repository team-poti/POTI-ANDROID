package com.poti.android.data.di

import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.data.auth.DefaultSocialLoginLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocialAuthModule {
    @Binds
    @Singleton
    abstract fun bindSocialLoginLauncher(
        launcher: DefaultSocialLoginLauncher,
    ): SocialLoginLauncher
}
