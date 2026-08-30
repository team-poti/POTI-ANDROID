package com.poti.android.presentation.user.withdrawal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.toast
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiListRadio
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.auth.WithdrawalReason
import com.poti.android.presentation.user.withdrawal.component.WithdrawalModal
import com.poti.android.presentation.user.withdrawal.component.WithdrawalUnavailableModal
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiEffect
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiIntent
import com.poti.android.presentation.user.withdrawal.model.WithdrawalUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun WithdrawalRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WithdrawalViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            WithdrawalUiEffect.NavigateBack -> onPopBackStack()
            is WithdrawalUiEffect.ShowError -> context.toast(effect.message)
        }
    }

    if (uiState.showWithdrawalModal) {
        WithdrawalModal(
            onDismissRequest = {
                viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalModalDismiss)
            },
            onDismissClick = {
                viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalModalDismiss)
            },
            onConfirmClick = {
                viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalConfirmClick)
            },
        )
    }

    if (uiState.showWithdrawalUnavailableModal) {
        WithdrawalUnavailableModal(
            onDismissRequest = {
                viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalUnavailableModalClose)
            },
            onConfirmClick = {
                viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalUnavailableModalClose)
            },
        )
    }

    WithdrawalScreen(
        uiState = uiState,
        onReasonSelect = { reason ->
            viewModel.processIntent(WithdrawalUiIntent.OnReasonSelect(reason))
        },
        onWithdrawalClick = {
            viewModel.processIntent(WithdrawalUiIntent.OnWithdrawalClick)
        },
        onBackClick = { viewModel.processIntent(WithdrawalUiIntent.OnBackClick) },
        modifier = modifier,
    )
}

@Composable
private fun WithdrawalScreen(
    uiState: WithdrawalUiState,
    onReasonSelect: (WithdrawalReason) -> Unit,
    onWithdrawalClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val withdrawalReasons = when (val state = uiState.withdrawalReasons) {
        is ApiState.Success -> state.data
        else -> persistentListOf()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PotiTheme.colors.white),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.withdrawal_title),
            containerColor = PotiTheme.colors.white,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.withdrawal_reason_label),
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                )

                PotiListRadio(
                    options = withdrawalReasons.map { it.label }.toImmutableList(),
                    selectedOptionIndex = withdrawalReasons.indexOf(uiState.selectedReason),
                    onClick = { index -> onReasonSelect(withdrawalReasons[index]) },
                )
            }

            PotiActionButton(
                text = stringResource(R.string.withdrawal_button),
                onClick = onWithdrawalClick,
                type = ActionButtonType.SECONDARY_MAIN,
                enabled = uiState.isWithdrawalEnabled,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 14.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawalScreenPreview() {
    WithdrawalScreen(
        uiState = WithdrawalUiState(),
        onReasonSelect = {},
        onWithdrawalClick = {},
        onBackClick = {},
    )
}
