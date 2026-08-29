package com.poti.android.core.designsystem.component.modal

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PotiPermissionModal(
    onDismiss: () -> Unit,
    onAllowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PotiLargeModal(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.alarm_permission_modal_title),
        text = stringResource(R.string.alarm_permission_modal_text),
        btnText = stringResource(R.string.alarm_permission_modal_button),
        onBtnClick = onAllowClick,
        subBtnText = stringResource(R.string.alarm_permission_modal_sub_button),
        onSubBtnClick = onDismiss,
        dismissOnClickOutside = false,
        topSlot = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_alarm),
                contentDescription = null,
                tint = PotiTheme.colors.poti600,
                modifier = Modifier.size(48.dp),
            )

            Spacer(Modifier.height(12.dp))
        },
        content = { Spacer(Modifier.height(24.dp)) },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PotiPermissionModalPreview() {
    PotiTheme {
        PotiPermissionModal(
            onDismiss = {},
            onAllowClick = {},
        )
    }
}
