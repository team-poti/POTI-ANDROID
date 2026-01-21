package com.poti.android.presentation.history.manage.component

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.theme.PotiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDeliveryBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirmClick: (deliveryMethod: String, trackingNumber: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var deliveryMethod by remember { mutableStateOf("") }
    var trackingNumber by remember { mutableStateOf("") }

    val isButtonEnabled = deliveryMethod.isNotBlank() && trackingNumber.isNotBlank()

    PotiBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        text = stringResource(R.string.history_delivery_bottomsheet_button),
        onClick = { onConfirmClick(deliveryMethod, trackingNumber) },
        enabled = isButtonEnabled,
        content = {
            BottomSheetContent(
                deliveryMethod = deliveryMethod,
                onDeliveryMethodChanged = { deliveryMethod = it },
                trackingNumber = trackingNumber,
                onTrackingNumberChanged = { trackingNumber = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            )
        },
    )
}

@Composable
private fun BottomSheetContent(
    deliveryMethod: String,
    onDeliveryMethodChanged: (String) -> Unit,
    trackingNumber: String,
    onTrackingNumberChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        PotiShortTextField(
            value = deliveryMethod,
            onValueChanged = onDeliveryMethodChanged,
            placeholder = stringResource(R.string.history_delivery_bottomsheet_method_placeholder),
            label = stringResource(R.string.history_delivery_bottomsheet_method_label),
            imeAction = ImeAction.Next,
            modifier = Modifier.padding(top = 12.dp),
        )
        PotiShortTextField(
            value = trackingNumber,
            onValueChanged = onTrackingNumberChanged,
            placeholder = stringResource(R.string.history_delivery_bottomsheet_tracking_placeholder),
            label = stringResource(R.string.history_delivery_bottomsheet_tracking_label),
            keyboardType = KeyboardType.Number,
            modifier = Modifier.padding(bottom = 226.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HistoryDeliveryBottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }

    PotiTheme {
        if (showBottomSheet) {
            HistoryDeliveryBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onConfirmClick = { i, j -> },
            )
        }
    }
}
