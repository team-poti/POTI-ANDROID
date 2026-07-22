package com.poti.android.core.auth

import android.content.Context
import com.poti.android.core.auth.kakao.KakaoLoginProvider
import com.poti.android.domain.model.auth.SocialType
import javax.inject.Inject

class DefaultSocialLoginLauncher @Inject constructor(
    private val kakaoLoginProvider: KakaoLoginProvider,
) : SocialLoginLauncher {
    override suspend fun login(
        context: Context,
        socialType: SocialType,
    ): SocialLoginResult =
        when (socialType) {
            SocialType.KAKAO -> kakaoLoginProvider.login(context)
            SocialType.GOOGLE -> SocialLoginResult.Failure(
                UnsupportedOperationException("Google login is not implemented"),
            )
        }
}
