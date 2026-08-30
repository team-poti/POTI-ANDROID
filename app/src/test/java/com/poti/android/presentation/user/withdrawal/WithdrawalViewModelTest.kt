package com.poti.android.presentation.user.withdrawal

import com.poti.android.domain.type.WithdrawalReasonType
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WithdrawalViewModelTest {
    private lateinit var viewModel: WithdrawalViewModel

    @Before
    fun setUp() {
        viewModel = WithdrawalViewModel()
    }

    @Test
    fun `withdrawal is disabled when no reason is selected`() {
        assertFalse(viewModel.uiState.value.isWithdrawalEnabled)
    }

    @Test
    fun `selecting a reason enables withdrawal`() {
        viewModel.processIntent(
            WithdrawalUiIntent.OnReasonSelect(WithdrawalReasonType.LOW_FREQUENCY),
        )

        assertEquals(
            WithdrawalReasonType.LOW_FREQUENCY,
            viewModel.uiState.value.selectedReason,
        )
        assertTrue(viewModel.uiState.value.isWithdrawalEnabled)
    }
}
