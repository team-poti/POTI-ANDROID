package com.poti.android.presentation.auth

import com.poti.android.MainDispatcherRule
import com.poti.android.core.auth.SocialLoginResult
import com.poti.android.domain.manager.AuthSessionManager
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.auth.WithdrawalReason
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.usecase.auth.ClearPendingReturnDeepLinkUseCase
import com.poti.android.domain.usecase.auth.EnterGuestModeUseCase
import com.poti.android.domain.usecase.auth.LoginUseCase
import com.poti.android.presentation.auth.model.LoginEffect
import com.poti.android.presentation.auth.model.LoginIntent
import com.poti.android.presentation.auth.model.LoginPhase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var authSessionManager: AuthSessionManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        authRepository = FakeAuthRepository()
        authSessionManager = AuthSessionManager()
        viewModel = LoginViewModel(
            loginUseCase = LoginUseCase(authRepository),
            enterGuestModeUseCase = EnterGuestModeUseCase(authSessionManager),
            clearPendingReturnDeepLinkUseCase = ClearPendingReturnDeepLinkUseCase(authSessionManager),
        )
    }

    @Test
    fun `emits one launch effect when login is clicked repeatedly`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val effects = collectEffects()

            viewModel.processIntent(LoginIntent.OnSocialLoginClick(SocialType.KAKAO))
            viewModel.processIntent(LoginIntent.OnSocialLoginClick(SocialType.KAKAO))
            advanceUntilIdle()

            assertEquals(listOf(LoginEffect.LaunchSocialLogin(SocialType.KAKAO)), effects)
            assertEquals(LoginPhase.SOCIAL_LOGIN, viewModel.uiState.value.phase)
        }

    @Test
    fun `enters guest mode and navigates to home once when browse is clicked repeatedly`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val effects = collectEffects()

            viewModel.processIntent(LoginIntent.OnBrowseAsGuestClick)
            viewModel.processIntent(LoginIntent.OnBrowseAsGuestClick)
            advanceUntilIdle()

            assertTrue(authSessionManager.isGuest.value)
            assertEquals(LoginPhase.SUCCESS, viewModel.uiState.value.phase)
            assertEquals(listOf(LoginEffect.NavigateToHome), effects)
        }

    @Test
    fun `requests server login once when social login succeeds`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startSocialLogin()

            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(1, authRepository.loginCallCount)
            assertEquals(SocialType.KAKAO, authRepository.lastSocialType)
            assertEquals(SOCIAL_ACCESS_TOKEN, authRepository.lastToken)
        }

    @Test
    fun `returns to idle when social login is cancelled`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startSocialLogin()

            viewModel.processIntent(
                LoginIntent.OnSocialLoginResult(
                    socialType = SocialType.KAKAO,
                    result = SocialLoginResult.Cancelled,
                ),
            )

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertEquals(0, authRepository.loginCallCount)
        }

    @Test
    fun `returns to idle when social login is aborted`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startSocialLogin()

            viewModel.processIntent(LoginIntent.OnSocialLoginAborted)

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertEquals(0, authRepository.loginCallCount)
        }

    @Test
    fun `returns to idle when social login fails`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startSocialLogin()

            viewModel.processIntent(
                LoginIntent.OnSocialLoginResult(
                    socialType = SocialType.KAKAO,
                    result = SocialLoginResult.Failure(RuntimeException("SDK login failed")),
                ),
            )

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertEquals(0, authRepository.loginCallCount)
        }

    @Test
    fun `ignores duplicate social result while server login is in progress`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startSocialLogin()

            viewModel.processIntent(socialLoginSuccessIntent())
            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(1, authRepository.loginCallCount)
        }

    @Test
    fun `returns to idle when server login fails`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.loginResult = Result.failure(RuntimeException("Server login failed"))
            startSocialLogin()

            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertEquals(1, authRepository.loginCallCount)
        }

    @Test
    fun `navigates to onboarding when server login returns new user`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.loginResult = Result.success(userAuth(isNewUser = true))
            val effects = collectEffects()
            startSocialLogin()

            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertTrue(effects.contains(LoginEffect.NavigateToOnboarding))
        }

    @Test
    fun `allows login again after navigating to onboarding`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.loginResult = Result.success(userAuth(isNewUser = true))
            val effects = collectEffects()
            startSocialLogin()
            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            viewModel.processIntent(LoginIntent.OnSocialLoginClick(SocialType.GOOGLE))
            advanceUntilIdle()

            assertEquals(LoginPhase.SOCIAL_LOGIN, viewModel.uiState.value.phase)
            assertTrue(effects.contains(LoginEffect.LaunchSocialLogin(SocialType.GOOGLE)))
        }

    @Test
    fun `navigates to home when server login returns existing user`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.loginResult = Result.success(userAuth(isNewUser = false))
            val effects = collectEffects()
            startSocialLogin()

            viewModel.processIntent(socialLoginSuccessIntent())
            advanceUntilIdle()

            assertEquals(LoginPhase.IDLE, viewModel.uiState.value.phase)
            assertTrue(effects.contains(LoginEffect.NavigateToHome))
        }

    private fun kotlinx.coroutines.test.TestScope.collectEffects(): MutableList<LoginEffect> {
        val effects = mutableListOf<LoginEffect>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.sideEffect.collect(effects::add)
        }
        return effects
    }

    private fun startSocialLogin() {
        viewModel.processIntent(LoginIntent.OnSocialLoginClick(SocialType.KAKAO))
    }

    private fun socialLoginSuccessIntent() = LoginIntent.OnSocialLoginResult(
        socialType = SocialType.KAKAO,
        result = SocialLoginResult.Success(SOCIAL_ACCESS_TOKEN),
    )

    private fun userAuth(isNewUser: Boolean) = UserAuth(
        accessToken = SERVER_ACCESS_TOKEN,
        refreshToken = SERVER_REFRESH_TOKEN,
        isNewUser = isNewUser,
        userId = USER_ID,
    )

    private class FakeAuthRepository : AuthRepository {
        var loginResult: Result<UserAuth> = Result.success(userAuth(isNewUser = false))
        var loginCallCount: Int = 0
            private set
        var lastSocialType: SocialType? = null
            private set
        var lastToken: String? = null
            private set

        override fun observeAuthState(): Flow<AuthState> = emptyFlow()

        override suspend fun login(
            socialType: SocialType,
            token: String,
        ): Result<UserAuth> {
            loginCallCount += 1
            lastSocialType = socialType
            lastToken = token
            return loginResult
        }

        override suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit> =
            Result.success(Unit)

        override suspend fun logout(): Result<Unit> = Result.success(Unit)

        override suspend fun getWithdrawalReasons(): Result<List<WithdrawalReason>> =
            error("Not used")

        override suspend fun withdrawal(reason: String): Result<Unit> {
            return Result.success(Unit)
        }
    }

    private companion object {
        const val SOCIAL_ACCESS_TOKEN = "social-access-token"
        const val SERVER_ACCESS_TOKEN = "server-access-token"
        const val SERVER_REFRESH_TOKEN = "server-refresh-token"
        const val USER_ID = 1L

        fun userAuth(isNewUser: Boolean) = UserAuth(
            accessToken = SERVER_ACCESS_TOKEN,
            refreshToken = SERVER_REFRESH_TOKEN,
            isNewUser = isNewUser,
            userId = USER_ID,
        )
    }
}
