package com.poti.android.presentation.history.manage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiInlineButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun ParticipantPayCheckContent(
    depositName: String,
    depositTime: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        TextWithLabel(
            label = stringResource(R.string.history_participant_field_type_deposit),
            text = "${depositName}\n$depositTime",
        )

        PotiInlineButton(
            text = stringResource(R.string.history_participant_field_deposit_label),
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            showIcon = false,
        )
    }
}

@Composable
fun ParticipantShippingContent(
    receiverName: String?,
    address: String?,
    phone: String?,
    modifier: Modifier = Modifier,
    trackingNumber: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(top = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        receiverName?.let {
            TextWithLabel(
                label = stringResource(R.string.history_participant_field_type_name),
                text = receiverName,
            )
        }

        address?.let {
            TextWithLabel(
                label = stringResource(R.string.history_participant_field_type_delivery),
                text = address,
            )
        }

        phone?.let {
            TextWithLabel(
                label = stringResource(R.string.history_participant_field_type_contact),
                text = phone,
            )
        }

        trackingNumber?.let { trackingNumber ->
            TextWithLabel(
                label = stringResource(R.string.history_participant_field_type_invoice),
                text = trackingNumber,
            )
        }

        onClick?.let {
            PotiInlineButton(
                text = stringResource(R.string.history_participant_field_delivery_label),
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                showIcon = false,
            )
        }
    }
}

@Composable
fun ParticipantDeliveredContent(
    trackingNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 32.dp),
    ) {
        TextWithLabel(
            label = stringResource(R.string.history_participant_field_type_invoice),
            text = trackingNumber,
        )
    }
}

@Composable
fun TextWithLabel(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = PotiTheme.typography.body14sb,
            color = PotiTheme.colors.black,
        )
        Text(
            text = text,
            style = PotiTheme.typography.body14m,
            color = PotiTheme.colors.black,
        )
    }
}
