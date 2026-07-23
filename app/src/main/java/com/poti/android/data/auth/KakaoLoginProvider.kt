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
            kakaoAuthClient.loginWithKakaoTalk(context) { result ->
                when (result) {
                    is KakaoAuthResult.Success -> {
                        onResult(SocialLoginResult.Success(result.accessToken))
                    }
                    KakaoAuthResult.Cancelled -> onResult(SocialLoginResult.Cancelled)
                    is KakaoAuthResult.Failure -> {
                        if (result.shouldFallbackToKakaoAccount()) {
                            loginWithKakaoAccount(context, onResult)
                        } else {
                            onResult(SocialLoginResult.Failure(result.cause))
                        }
                    }
                    is KakaoAuthResult.InvalidResponse -> {
                        onResult(SocialLoginResult.Failure(result.cause))
                    }
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
        kakaoAuthClient.loginWithKakaoAccount(context) { result ->
            when (result) {
                is KakaoAuthResult.Success -> {
                    onResult(SocialLoginResult.Success(result.accessToken))
                }
                KakaoAuthResult.Cancelled -> onResult(SocialLoginResult.Cancelled)
                is KakaoAuthResult.Failure -> {
                    onResult(SocialLoginResult.Failure(result.cause))
                }
                is KakaoAuthResult.InvalidResponse -> {
                    onResult(SocialLoginResult.Failure(result.cause))
                }
            }
        }
    }

    private fun KakaoAuthResult.Failure.shouldFallbackToKakaoAccount(): Boolean = true
}
