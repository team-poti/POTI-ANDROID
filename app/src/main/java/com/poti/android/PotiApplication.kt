package com.poti.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.poti.android.core.fcm.FcmMessagingService
import com.poti.android.core.fcm.FcmTokenProvider
import com.poti.android.core.fcm.repository.FcmRepository
import com.poti.android.di.ApplicationScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PotiApplication : Application() {
    @Inject
    lateinit var fcmTokenProvider: FcmTokenProvider

    @Inject
    lateinit var fcmRepository: FcmRepository

    @ApplicationScope
    @Inject
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        setDarkMode()
        setTimber()
        initKakaoSdk()
        FcmMessagingService.createChannels(this)
        syncFcmToken()
    }

    private fun setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun syncFcmToken() {
        applicationScope.launch {
            val token = fcmTokenProvider.getToken() ?: return@launch
            fcmRepository.saveFcmToken(token)
                .onFailure { Timber.e(it, "Failed to save FCM token") }
        }
    }
}
