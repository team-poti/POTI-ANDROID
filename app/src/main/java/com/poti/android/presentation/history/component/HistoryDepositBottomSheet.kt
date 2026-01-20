package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.theme.PotiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDepositBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirmClick: (depositor: String, depositTime: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var depositor by remember { mutableStateOf("") }
    var depositTime by remember { mutableStateOf("") }

    PotiBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        text = stringResource(R.string.history_deposit_bottomsheet_button),
        onClick = { onConfirmClick(depositor, depositTime) },
        content = {
            BottomSheetContent(
                depositor = depositor,
                onDepositorChanged = { depositor = it },
                depositTime = depositTime,
                onDepositTimeChanged = { depositTime = it },
                modifier = modifier
                    .padding(horizontal = 16.dp),
            )
        },
    )
}

@Composable
private fun BottomSheetContent(
    depositor: String,
    onDepositorChanged: (String) -> Unit,
    depositTime: String,
    onDepositTimeChanged: (String) -> Unit,
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
            modifier = Modifier.padding(bottom = 226.dp),
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
