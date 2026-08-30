package com.poti.android.presentation.user.withdrawal

import com.poti.android.MainDispatcherRule
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.network.model.NetworkError
import com.poti.android.domain.model.auth.AuthState
import com.poti.android.domain.model.auth.SocialType
import com.poti.android.domain.model.auth.UserAuth
import com.poti.android.domain.model.auth.WithdrawalReason
import com.poti.android.domain.repository.AuthRepository
import com.poti.android.domain.usecase.auth.GetWithdrawalReasonsUseCase
import com.poti.android.domain.usecase.auth.WithdrawalUseCase
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WithdrawalViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var viewModel: WithdrawalViewModel

    @Before
    fun setUp() {
        authRepository = FakeAuthRepository()
        viewModel = WithdrawalViewModel(
            getWithdrawalReasonsUseCase = GetWithdrawalReasonsUseCase(authRepository),
            withdrawalUseCase = WithdrawalUseCase(authRepository),
        )
    }

    @Test
    fun `withdrawal is disabled when no reason is selected`() {
        assertFalse(viewModel.uiState.value.isWithdrawalEnabled)
    }

    @Test
    fun `loads withdrawal reasons on initialization`() =
        runTest(mainDispatcherRule.testDispatcher) {
            advanceUntilIdle()

            val state = viewModel.uiState.value.withdrawalReasons as ApiState.Success
            assertEquals(listOf(WITHDRAWAL_REASON), state.data)
        }

    @Test
    fun `selecting a reason enables withdrawal`() {
        viewModel.processIntent(
            WithdrawalUiIntent.OnReasonSelect(WITHDRAWAL_REASON),
        )

        assertEquals(
            WITHDRAWAL_REASON,
            viewModel.uiState.value.selectedReason,
        )
        assertTrue(viewModel.uiState.value.isWithdrawalEnabled)
    }

    @Test
    fun `clicking withdrawal after selecting a reason shows confirmation modal`() {
        selectReason()

        viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalClick)

        assertTrue(viewModel.uiState.value.showWithdrawalModal)
    }

    @Test
    fun `confirming withdrawal requests withdrawal only once`() =
        runTest(mainDispatcherRule.testDispatcher) {
            selectReason()
            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalClick)

            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalConfirmClick)
            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalConfirmClick)
            advanceUntilIdle()

            assertEquals(1, authRepository.withdrawalCallCount)
            assertEquals(WITHDRAWAL_REASON.code, authRepository.lastWithdrawalReason)
        }

    @Test
    fun `40019 response shows withdrawal unavailable modal`() =
        runTest(mainDispatcherRule.testDispatcher) {
            authRepository.withdrawalResult = Result.failure(
                NetworkError.BadRequest(
                    code = 40019,
                    serverMsg = "진행 중인 거래가 있어 탈퇴할 수 없습니다.",
                ),
            )
            selectReason()
            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalClick)

            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalConfirmClick)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.showWithdrawalModal)
            assertTrue(viewModel.uiState.value.showWithdrawalUnavailableModal)
        }

    private fun selectReason() {
        viewModel.processIntent(
            WithdrawalUiIntent.OnReasonSelect(WITHDRAWAL_REASON),
        )
    }

    private class FakeAuthRepository : AuthRepository {
        var withdrawalResult: Result<Unit> = Result.success(Unit)
        var withdrawalCallCount: Int = 0
            private set
        var lastWithdrawalReason: String? = null
            private set

        override fun observeAuthState(): Flow<AuthState> = emptyFlow()

        override suspend fun login(
            socialType: SocialType,
            token: String,
        ): Result<UserAuth> = error("Not used")

        override suspend fun saveOnboardingState(isCompleted: Boolean): Result<Unit> =
            error("Not used")

        override suspend fun logout(): Result<Unit> = error("Not used")

        override suspend fun getWithdrawalReasons(): Result<List<WithdrawalReason>> =
            Result.success(listOf(WITHDRAWAL_REASON))

        override suspend fun withdrawal(reason: String): Result<Unit> {
            withdrawalCallCount += 1
            lastWithdrawalReason = reason
            return withdrawalResult
        }
    }

    private companion object {
        val WITHDRAWAL_REASON = WithdrawalReason(
            code = "LOW_FREQUENCY",
            label = "이용 빈도가 낮아요.",
        )
    }
}
