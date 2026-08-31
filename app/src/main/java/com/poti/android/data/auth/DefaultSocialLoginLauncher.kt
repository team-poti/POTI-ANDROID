package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginLauncher
import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.data.local.datasource.WithdrawalLocalDataCleaner
import com.poti.android.domain.model.auth.SocialType
import javax.inject.Inject

class DefaultSocialLoginLauncher @Inject constructor(
    private val kakaoLoginProvider: KakaoLoginProvider,
    private val googleLoginProvider: GoogleLoginProvider,
    private val withdrawalLocalDataCleaner: WithdrawalLocalDataCleaner,
) : SocialLoginLauncher {
    override suspend fun login(
        context: Context,
        socialType: SocialType,
    ): SocialLoginResult {
        withdrawalLocalDataCleaner.awaitCacheCleanup()

        return when (socialType) {
            SocialType.KAKAO -> kakaoLoginProvider.login(context)
            SocialType.GOOGLE -> googleLoginProvider.login(context)
        }
    }
}
