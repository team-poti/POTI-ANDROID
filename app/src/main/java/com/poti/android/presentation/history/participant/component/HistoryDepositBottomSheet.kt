package com.poti.android.presentation.history.participant.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.theme.PotiTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistoryDepositBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirmClick: (depositor: String, depositTime: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var depositor by remember { mutableStateOf("") }
    var depositTime by remember { mutableStateOf("") }
    val isKeyboardVisible = WindowInsets.isImeVisible
    val bottomPadding = remember(isKeyboardVisible) {
        if (isKeyboardVisible) 60.dp else 226.dp
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    PotiBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        text = stringResource(R.string.history_deposit_bottomsheet_button),
        onClick = { onConfirmClick(depositor, depositTime) },
        enabled = depositor.isNotBlank() && depositTime.isNotBlank(),
    ) {
        BottomSheetContent(
            depositor = depositor,
            onDepositorChanged = { depositor = it },
            depositTime = depositTime,
            onDepositTimeChanged = { depositTime = it },
            bottomPadding = bottomPadding,
            modifier = Modifier
                .padding(
                    horizontal = screenWidthDp(16.dp),
                ),
        )
    }
}

@Composable
private fun BottomSheetContent(
    depositor: String,
    onDepositorChanged: (String) -> Unit,
    depositTime: String,
    onDepositTimeChanged: (String) -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        PotiShortTextField(
            value = depositor,
            onValueChanged = onDepositorChanged,
            placeholder = stringResource(R.string.history_deposit_bottomsheet_depositor_placeholder),
            label = stringResource(R.string.history_deposit_bottomsheet_depositor_label),
            imeAction = ImeAction.Next,
        )
        PotiShortTextField(
            value = depositTime,
            onValueChanged = onDepositTimeChanged,
            placeholder = stringResource(R.string.history_deposit_bottomsheet_deposit_time_placeholder),
            label = stringResource(R.string.history_deposit_bottomsheet_deposit_time_label),
            keyboardType = KeyboardType.Number,
            modifier = Modifier.padding(bottom = bottomPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PotiBottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }

    PotiTheme {
        if (showBottomSheet) {
            HistoryDepositBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onConfirmClick = { i, j -> },
            )
        }
    }
}
