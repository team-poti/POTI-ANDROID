package com.poti.android.presentation.user.withdrawal.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.R
import com.poti.android.core.designsystem.component.modal.PotiSmallModal

@Composable
fun WithdrawalModal(modifier: Modifier = Modifier) {
    PotiSmallModal(
        onDismissRequest = {},
        title = stringResource(R.string.withdrawal_confirm_modal_title),
        text = stringResource(R.string.withdrawal_confirm_modal_text),
        dismissBtnText = stringResource(R.string.withdrawal_confirm_modal_dismiss),
        confirmBtnText = stringResource(R.string.withdrawal_confirm_modal_confirm),
        onDismissBtnClick = {},
        onConfirmBtnClick = {},
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun WithdrawalModalPreview() {
    WithdrawalModal()
}
