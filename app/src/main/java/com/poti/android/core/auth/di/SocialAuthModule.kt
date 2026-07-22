package com.poti.android.core.auth.di

import com.poti.android.core.auth.DefaultSocialLoginLauncher
import com.poti.android.core.auth.SocialLoginLauncher
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
