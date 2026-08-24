package com.poti.android.presentation.user.account

import com.poti.android.MainDispatcherRule
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.user.UserAccount
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.repository.UserRepository
import com.poti.android.domain.usecase.auth.LogoutUseCase
import com.poti.android.domain.usecase.user.GetUserAccountUseCase
import com.poti.android.presentation.user.account.model.AccountSettingUiIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class AccountSettingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var viewModel: AccountSettingViewModel

    @Before
    fun setUp() {
        val userRepository = mock(UserRepository::class.java)
        runBlocking {
            `when`(userRepository.getUserAccount()).thenReturn(Result.success(USER_ACCOUNT))
        }
        authRepository = FakeAuthRepository()
        viewModel = AccountSettingViewModel(
            getUserAccountUseCase = GetUserAccountUseCase(userRepository),
            logoutUseCase = LogoutUseCase(authRepository),
        )
    }

    @Test
    fun `requests logout only once when logout is clicked repeatedly`() =
        runTest(mainDispatcherRule.testDispatcher) {
            viewModel.processIntent(AccountSettingUiIntent.OnLogoutClick)
            viewModel.processIntent(AccountSettingUiIntent.OnLogoutClick)
            advanceUntilIdle()

            assertEquals(1, authRepository.logoutCallCount)
            assertTrue(viewModel.uiState.value.isLoggingOut)
        }

    @Test
    fun `allows logout retry when logout fails`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.logoutResult = Result.failure(IllegalStateException("logout failed"))

            viewModel.processIntent(AccountSettingUiIntent.OnLogoutClick)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoggingOut)

            viewModel.processIntent(AccountSettingUiIntent.OnLogoutClick)
            advanceUntilIdle()

            assertEquals(2, authRepository.logoutCallCount)
        }

    private class FakeAuthRepository : AuthRepository {
        var logoutResult: Result<Unit> = Result.success(Unit)
        var logoutCallCount: Int = 0
            private set

        override fun observeAuthState(): Flow<AuthState> = emptyFlow()

        override suspend fun login(
            socialType: SocialType,
            token: String,
        ): Result<UserAuth> = error("Not used")

        override suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit> =
            error("Not used")

        override suspend fun logout(): Result<Unit> {
            logoutCallCount += 1
            return logoutResult
        }

        override suspend fun withdrawal(): Result<Unit> = error("Not used")
    }

    private companion object {
        val USER_ACCOUNT = UserAccount(
            nickname = "poti",
            email = "poti@example.com",
            socialType = SocialType.KAKAO,
        )
    }
}
