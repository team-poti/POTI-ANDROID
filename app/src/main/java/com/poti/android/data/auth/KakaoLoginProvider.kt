package com.poti.android.data.auth

import android.content.Context
import com.poti.android.core.auth.SocialLoginResult
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
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

            startLogin(
                context = context,
                isActive = { continuation.isActive },
                onResult = onResult,
            )
        }

    private fun startLogin(
        context: Context,
        isActive: () -> Boolean,
        onResult: (SocialLoginResult) -> Unit,
    ) {
        if (!isActive()) return

        if (kakaoAuthClient.isKakaoTalkLoginAvailable(context)) {
            kakaoAuthClient.loginWithKakaoTalk(context) { result ->
                if (!isActive()) return@loginWithKakaoTalk

                when (result) {
                    is KakaoAuthResult.Success -> {
                        onResult(SocialLoginResult.Success(result.accessToken))
                    }
                    KakaoAuthResult.Cancelled -> onResult(SocialLoginResult.Cancelled)
                    is KakaoAuthResult.Failure -> {
                        if (!isActive()) return@loginWithKakaoTalk

                        Timber.w(
                            result.cause,
                            "KakaoTalk login failed; falling back to Kakao account login",
                        )
                        loginWithKakaoAccount(context, isActive, onResult)
                    }
                    is KakaoAuthResult.InvalidResponse -> {
                        onResult(SocialLoginResult.Failure(result.cause))
                    }
                }
            }
        } else {
            loginWithKakaoAccount(context, isActive, onResult)
        }
    }

    private fun loginWithKakaoAccount(
        context: Context,
        isActive: () -> Boolean,
        onResult: (SocialLoginResult) -> Unit,
    ) {
        if (!isActive()) return

        kakaoAuthClient.loginWithKakaoAccount(context) { result ->
            if (!isActive()) return@loginWithKakaoAccount

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
}
