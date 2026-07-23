package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginResult
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

class KakaoLoginProvider @Inject constructor(
    private val kakaoAuthClient: KakaoAuthClient,
) {
    suspend fun login(context: Context): SocialLoginResult =
        try {
            awaitLogin(context)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (error: Exception) {
            SocialLoginResult.Failure(error)
        }

    private suspend fun awaitLogin(context: Context): SocialLoginResult =
        suspendCancellableCoroutine { continuation ->
            val onResult: (SocialLoginResult) -> Unit = { result ->
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }

            startLogin(context, onResult)
        }

    private fun startLogin(
        context: Context,
        onResult: (SocialLoginResult) -> Unit,
    ) {
        if (kakaoAuthClient.isKakaoTalkLoginAvailable(context)) {
            kakaoAuthClient.loginWithKakaoTalk(context) { accessToken, error ->
                when {
                    accessToken != null -> onResult(SocialLoginResult.Success(accessToken))
                    error.isCancelled() -> onResult(SocialLoginResult.Cancelled)
                    error != null && error.shouldFallbackToKakaoAccount() -> {
                        loginWithKakaoAccount(context, onResult)
                    }
                    error != null -> onResult(SocialLoginResult.Failure(error))
                    else -> onResult(
                        SocialLoginResult.Failure(
                            IllegalStateException("Kakao login returned neither token nor error"),
                        ),
                    )
                }
            }
        } else {
            loginWithKakaoAccount(context, onResult)
        }
    }

    private fun loginWithKakaoAccount(
        context: Context,
        onResult: (SocialLoginResult) -> Unit,
    ) {
        kakaoAuthClient.loginWithKakaoAccount(context) { accessToken, error ->
            when {
                accessToken != null -> onResult(SocialLoginResult.Success(accessToken))
                error.isCancelled() -> onResult(SocialLoginResult.Cancelled)
                error != null -> onResult(SocialLoginResult.Failure(error))
                else -> onResult(
                    SocialLoginResult.Failure(
                        IllegalStateException("Kakao account login returned neither token nor error"),
                    ),
                )
            }
        }
    }

    private fun Throwable?.isCancelled(): Boolean =
        kakaoAuthClient.isCancelled(this)

    private fun Throwable.shouldFallbackToKakaoAccount(): Boolean =
        !isCancelled()
}
