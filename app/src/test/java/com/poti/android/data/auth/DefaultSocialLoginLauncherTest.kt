package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.domain.model.auth.SocialType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

class DefaultSocialLoginLauncherTest {
    private val context: Context = mock(Context::class.java)

    @Test
    fun `delegates to Kakao provider when social type is Kakao`() = runBlocking {
        val client = TrackingKakaoAuthClient(accessToken = KAKAO_ACCESS_TOKEN)
        val launcher = DefaultSocialLoginLauncher(KakaoLoginProvider(client))

        val result = launcher.login(context, SocialType.KAKAO)

        assertEquals(SocialLoginResult.Success(KAKAO_ACCESS_TOKEN), result)
        assertEquals(1, client.isKakaoTalkLoginAvailableCallCount)
        assertEquals(1, client.kakaoTalkLoginCallCount)
    }

    @Test
    fun `does not delegate to Kakao provider when social type is Google`() = runBlocking {
        val client = TrackingKakaoAuthClient(accessToken = KAKAO_ACCESS_TOKEN)
        val launcher = DefaultSocialLoginLauncher(KakaoLoginProvider(client))

        val result = launcher.login(context, SocialType.GOOGLE)

        require(result is SocialLoginResult.Failure)
        assertTrue(result.cause is UnsupportedOperationException)
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
            onResult: (accessToken: String?, error: Throwable?) -> Unit,
        ) {
            kakaoTalkLoginCallCount += 1
            onResult(accessToken, null)
        }

        override fun loginWithKakaoAccount(
            context: Context,
            onResult: (accessToken: String?, error: Throwable?) -> Unit,
        ) {
            kakaoAccountLoginCallCount += 1
            onResult(accessToken, null)
        }

        override fun isCancelled(error: Throwable?): Boolean = false
    }

    private companion object {
        const val KAKAO_ACCESS_TOKEN = "kakao-access-token"
    }
}
