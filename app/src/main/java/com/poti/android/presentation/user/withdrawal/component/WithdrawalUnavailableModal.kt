package com.poti.android.presentation.user.withdrawal.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.ModalButtonType
import com.poti.android.core.designsystem.component.button.PotiModalButton
import com.poti.android.core.designsystem.component.modal.PotiModal
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun WithdrawalUnavailableModal(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PotiModal(
        onDismissRequest = onDismissRequest,
        modifier = modifier.padding(horizontal = 36.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 36.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_notice_lg),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .size(48.dp),
                tint = PotiTheme.colors.sementicRed,
            )

            Text(
                text = stringResource(R.string.withdrawal_unavailable_modal_title),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.title18sb,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(R.string.withdrawal_unavailable_modal_text),
                modifier = Modifier.padding(top = 8.dp),
                color = PotiTheme.colors.gray800,
                style = PotiTheme.typography.body14m,
                textAlign = TextAlign.Center,
            )

            PotiModalButton(
                text = stringResource(R.string.withdrawal_unavailable_modal_confirm),
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 27.dp),
                type = ModalButtonType.SECONDARY,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawalUnavailableModalPreview() {
    PotiTheme {
        WithdrawalUnavailableModal(
            onDismissRequest = {},
            onConfirmClick = {},
        )
    }
}
