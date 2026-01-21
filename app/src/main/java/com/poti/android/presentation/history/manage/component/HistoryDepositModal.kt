package com.poti.android.presentation.history.manage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.R
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun HistoryDepositConfirmModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    PotiSmallModal(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.history_deposit_confirm_modal_title),
        text = stringResource(R.string.history_deposit_confirm_modal_text),
        dismissBtnText = stringResource(R.string.history_deposit_confirm_modal_dismiss),
        confirmBtnText = stringResource(R.string.history_deposit_confirm_modal_confirm),
        onDismissBtnClick = onDismiss,
        onConfirmBtnClick = onConfirm,
    )
}

@Preview
@Composable
private fun HistoryDepositConfirmModalPreview() {
    PotiTheme {
        HistoryDepositConfirmModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
