package com.poti.android.presentation.history.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiProfileSummary
import com.poti.android.core.designsystem.component.display.PotiProfileSummarySize
import com.poti.android.core.designsystem.component.modal.PotiLargeModal
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun HistoryDeliveryConfirmModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    PotiSmallModal(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.history_delivery_confirm_modal_title),
        text = stringResource(R.string.history_delivery_confirm_modal_text),
        dismissBtnText = stringResource(R.string.history_delivery_confirm_modal_dismiss),
        confirmBtnText = stringResource(R.string.history_delivery_confirm_modal_confirm),
        onDismissBtnClick = onDismiss,
        onConfirmBtnClick = onConfirm,
    )
}

@Composable
fun HistoryDeliveryReviewModal(
    partnerNickname: String,
    partnerProfileUrl: String?,
    partnerRating: String,
    onConfirm: (Int) -> Unit,
    onSkip: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    var currentRating by remember { mutableIntStateOf(0) }

    PotiLargeModal(
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.history_delivery_review_modal_title),
        text = stringResource(R.string.history_delivery_review_modal_text),
        btnText = stringResource(R.string.history_delivery_review_modal_button),
        onBtnClick = { if(currentRating != 0) onConfirm(currentRating) },
        subBtnText = stringResource(R.string.history_delivery_review_modal_sub_button),
        onSubBtnClick = onSkip,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 28.dp)
                .border(
                    width = 1.dp,
                    color = PotiTheme.colors.gray300,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(10.dp),
        ) {
            PotiProfileSummary(
                profileImageUrl = partnerProfileUrl,
                nickname = partnerNickname,
                sizeType = PotiProfileSummarySize.SMALL,
                rating = partnerRating,
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.spacedBy((-6).dp),
        ) {
            repeat(5) { index ->
                val starIndex = index + 1
                val isSelected = starIndex <= currentRating
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { currentRating = starIndex },
                        ),
                    tint = if (isSelected) PotiTheme.colors.poti600 else PotiTheme.colors.gray300,
                )
            }
        }
    }
}

@Preview
@Composable
private fun HistoryDeliveryConfirmModalPreview() {
    PotiTheme {
        HistoryDeliveryConfirmModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun HistoryDeliveryReviewModalPreview() {
    PotiTheme {
        HistoryDeliveryReviewModal(
            partnerNickname = "닉네임",
            partnerProfileUrl = null,
            partnerRating = "4.8",
            onConfirm = {},
            onSkip = {},
            onDismissRequest = {},
        )
    }
}
