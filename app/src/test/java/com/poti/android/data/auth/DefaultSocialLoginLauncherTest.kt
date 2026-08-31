package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.data.local.datasource.WithdrawalLocalDataCleaner
import com.poti.android.domain.model.auth.SocialType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DefaultSocialLoginLauncherTest {
    private val context: Context = mock(Context::class.java)
    private val withdrawalLocalDataCleaner = mock(WithdrawalLocalDataCleaner::class.java)

    @Test
    fun `delegates to Kakao provider when social type is Kakao`() = runBlocking {
        val client = TrackingKakaoAuthClient(accessToken = KAKAO_ACCESS_TOKEN)
        val googleLoginProvider = mock(GoogleLoginProvider::class.java)
        val launcher = DefaultSocialLoginLauncher(
            KakaoLoginProvider(client),
            googleLoginProvider,
            withdrawalLocalDataCleaner,
        )

        val result = launcher.login(context, SocialType.KAKAO)

        assertEquals(SocialLoginResult.Success(KAKAO_ACCESS_TOKEN), result)
        assertEquals(1, client.isKakaoTalkLoginAvailableCallCount)
        assertEquals(1, client.kakaoTalkLoginCallCount)
    }

    @Test
    fun `delegates to Google provider when social type is Google`() = runBlocking {
        val client = TrackingKakaoAuthClient(accessToken = KAKAO_ACCESS_TOKEN)
        val googleLoginProvider = mock(GoogleLoginProvider::class.java)
        val googleResult = SocialLoginResult.Success(GOOGLE_ID_TOKEN)
        `when`(googleLoginProvider.login(context)).thenReturn(googleResult)
        val launcher = DefaultSocialLoginLauncher(
            KakaoLoginProvider(client),
            googleLoginProvider,
            withdrawalLocalDataCleaner,
        )

        val result = launcher.login(context, SocialType.GOOGLE)

        assertEquals(googleResult, result)
        assertEquals(0, client.isKakaoTalkLoginAvailableCallCount)
        assertEquals(0, client.kakaoTalkLoginCallCount)
        assertEquals(0, client.kakaoAccountLoginCallCount)
    }

    private class TrackingKakaoAuthClient(
        private val accessToken: String,
    ) : KakaoAuthClient {
        var isKakaoTalkLoginAvailableCallCount: Int = 0
            private set

        var kakaoTalkLoginCallCount: Int = 0
            private set

        var kakaoAccountLoginCallCount: Int = 0
            private set

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean {
            isKakaoTalkLoginAvailableCallCount += 1
            return true
        }

        override fun loginWithKakaoTalk(
            context: Context,
            onResult: (KakaoAuthResult) -> Unit,
        ) {
            kakaoTalkLoginCallCount += 1
            onResult(KakaoAuthResult.Success(accessToken))
        }

        override fun loginWithKakaoAccount(
            context: Context,
            onResult: (KakaoAuthResult) -> Unit,
        ) {
            kakaoAccountLoginCallCount += 1
            onResult(KakaoAuthResult.Success(accessToken))
        }
    }

    private companion object {
        const val KAKAO_ACCESS_TOKEN = "kakao-access-token"
        const val GOOGLE_ID_TOKEN = "google-id-token"
    }
}
