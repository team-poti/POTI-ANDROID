package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.mockito.Mockito.mock

class KakaoLoginProviderTest {
    private val context: Context = mock(Context::class.java)

    @Test
    fun `returns access token when KakaoTalk login succeeds`() = runBlocking {
        val client = FakeKakaoAuthClient(
            kakaoTalkResult = KakaoAuthResponse(accessToken = KAKAO_ACCESS_TOKEN),
        )
        val provider = KakaoLoginProvider(client)

        val result = provider.login(context)

        assertEquals(SocialLoginResult.Success(KAKAO_ACCESS_TOKEN), result)
        assertEquals(1, client.kakaoTalkLoginCallCount)
        assertEquals(0, client.kakaoAccountLoginCallCount)
    }

    @Test
    fun `returns cancelled without fallback when KakaoTalk login is cancelled`() = runBlocking {
        val cancellation = RuntimeException("cancelled")
        val client = FakeKakaoAuthClient(
            kakaoTalkResult = KakaoAuthResponse(error = cancellation),
            cancelledError = cancellation,
        )
        val provider = KakaoLoginProvider(client)

        val result = provider.login(context)

        assertSame(SocialLoginResult.Cancelled, result)
        assertEquals(1, client.kakaoTalkLoginCallCount)
        assertEquals(0, client.kakaoAccountLoginCallCount)
    }

    @Test
    fun `falls back to Kakao account when KakaoTalk login fails`() = runBlocking {
        val client = FakeKakaoAuthClient(
            kakaoTalkResult = KakaoAuthResponse(error = RuntimeException("talk login failed")),
            kakaoAccountResult = KakaoAuthResponse(accessToken = KAKAO_ACCOUNT_ACCESS_TOKEN),
        )
        val provider = KakaoLoginProvider(client)

        val result = provider.login(context)

        assertEquals(SocialLoginResult.Success(KAKAO_ACCOUNT_ACCESS_TOKEN), result)
        assertEquals(1, client.kakaoTalkLoginCallCount)
        assertEquals(1, client.kakaoAccountLoginCallCount)
    }

    @Test
    fun `returns account error when fallback login fails`() = runBlocking {
        val accountError = RuntimeException("account login failed")
        val client = FakeKakaoAuthClient(
            kakaoTalkResult = KakaoAuthResponse(error = RuntimeException("talk login failed")),
            kakaoAccountResult = KakaoAuthResponse(error = accountError),
        )
        val provider = KakaoLoginProvider(client)

        val result = provider.login(context)

        require(result is SocialLoginResult.Failure)
        assertSame(accountError, result.cause)
        assertEquals(1, client.kakaoTalkLoginCallCount)
        assertEquals(1, client.kakaoAccountLoginCallCount)
    }

    private data class KakaoAuthResponse(
        val accessToken: String? = null,
        val error: Throwable? = null,
    )

    private class FakeKakaoAuthClient(
        private val isKakaoTalkAvailable: Boolean = true,
        private val kakaoTalkResult: KakaoAuthResponse = KakaoAuthResponse(),
        private val kakaoAccountResult: KakaoAuthResponse = KakaoAuthResponse(),
        private val cancelledError: Throwable? = null,
    ) : KakaoAuthClient {
        var kakaoTalkLoginCallCount: Int = 0
            private set

        var kakaoAccountLoginCallCount: Int = 0
            private set

        override fun isKakaoTalkLoginAvailable(context: Context): Boolean = isKakaoTalkAvailable

        override fun loginWithKakaoTalk(
            context: Context,
            onResult: (accessToken: String?, error: Throwable?) -> Unit,
        ) {
            kakaoTalkLoginCallCount += 1
            onResult(kakaoTalkResult.accessToken, kakaoTalkResult.error)
        }

        override fun loginWithKakaoAccount(
            context: Context,
            onResult: (accessToken: String?, error: Throwable?) -> Unit,
        ) {
            kakaoAccountLoginCallCount += 1
            onResult(kakaoAccountResult.accessToken, kakaoAccountResult.error)
        }

        override fun isCancelled(error: Throwable?): Boolean = error === cancelledError
    }

    private companion object {
        const val KAKAO_ACCESS_TOKEN = "kakao-access-token"
        const val KAKAO_ACCOUNT_ACCESS_TOKEN = "kakao-account-access-token"
    }
}
