package com.poti.android.presentation.history.manage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.manage.component.HistoryDeliveryBottomSheet
import com.poti.android.presentation.history.manage.component.HistoryDepositConfirmModal
import com.poti.android.presentation.history.manage.component.HistoryParticipantDropdown
import com.poti.android.presentation.history.manage.component.ParticipantDeliveredContent
import com.poti.android.presentation.history.manage.component.ParticipantPayCheckContent
import com.poti.android.presentation.history.manage.component.ParticipantShippingContent
import com.poti.android.presentation.history.manage.model.ManageModalState
import com.poti.android.presentation.history.manage.model.ParticipantManageUiEffect
import com.poti.android.presentation.history.manage.model.ParticipantManageUiIntent
import com.poti.android.presentation.history.manage.model.RecruiterManageDetailUiModel

@Composable
fun ParticipantManageRoute(
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ParticipantManageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            ParticipantManageUiEffect.NavigateBack -> popBackStack()
        }
    }

    when (val modal = uiState.activeModal) {
        is ManageModalState.DepositConfirm -> {
            HistoryDepositConfirmModal(
                onConfirm = { viewModel.processIntent(ParticipantManageUiIntent.OnDepositModalConfirm(modal.participantId)) },
                onDismiss = { viewModel.processIntent(ParticipantManageUiIntent.OnDepositModalDismiss) },
            )
        }
        is ManageModalState.DeliveryInput -> {
            HistoryDeliveryBottomSheet(
                onDismissRequest = { viewModel.processIntent(ParticipantManageUiIntent.OnDepositModalDismiss) },
                onConfirmClick = { deliveryMethod, trackingNumber ->
                    viewModel.processIntent(
                        ParticipantManageUiIntent.RegisterDelivery(
                            participantId = modal.participantId,
                            deliveryMethod = deliveryMethod,
                            trackingNumber = trackingNumber,
                        ),
                    )
                },
            )
        }
        ManageModalState.None -> {}
    }

    uiState.participantManageDetailLoadState.onSuccess { participants ->
        ParticipantManageScreen(
            uiState = participants,
            onBackClick = { viewModel.processIntent(ParticipantManageUiIntent.OnBackClick) },
            onConfirmDepositClick = { viewModel.processIntent(ParticipantManageUiIntent.OnDepositConfirmClick(it)) },
            onInputTrackingNumberClick = { viewModel.processIntent(ParticipantManageUiIntent.OnDeliveryInputClick(it)) },
            modifier = modifier,
        )
    }
}

@Composable
private fun ParticipantManageScreen(
    uiState: RecruiterManageDetailUiModel,
    onBackClick: () -> Unit,
    onConfirmDepositClick: (Long) -> Unit,
    onInputTrackingNumberClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedIds = remember(uiState) { mutableStateListOf<Long>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.history_participant_management_title),
        )
        PotiDivider(styleType = PotiDividerStyle.SMALL)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .animateContentSize(),
        ) {
            uiState.participants.forEachIndexed { index, participant ->

                val isExpanded = participant.userId in expandedIds

                HistoryParticipantDropdown(
                    participant = participant,
                    isExpanded = isExpanded,
                    onToggle = {
                        if (isExpanded) {
                            expandedIds.remove(participant.userId)
                        } else {
                            expandedIds.add(participant.userId)
                        }
                    },
                ) {
                    when (participant.participantStatus) {
                        ParticipantStatusType.WAIT_PAY_CHECK -> {
                            participant.depositInfo?.let { depositInfo ->
                                ParticipantPayCheckContent(
                                    depositName = depositInfo.depositorName,
                                    depositTime = depositInfo.depositTime,
                                    onClick = { onConfirmDepositClick(participant.userId) },
                                )
                            }
                        }
                        ParticipantStatusType.PAID -> {
                            participant.shippingInfo?.let { shippingInfo ->
                                ParticipantShippingContent(
                                    receiverName = shippingInfo.receiverName,
                                    address = shippingInfo.address,
                                    phone = shippingInfo.phone,
                                    onClick = { onInputTrackingNumberClick(participant.userId) },
                                )
                            }
                        }
                        ParticipantStatusType.SHIPPED -> {
                            participant.shippingInfo?.let { shippingInfo ->
                                ParticipantShippingContent(
                                    receiverName = shippingInfo.receiverName,
                                    address = shippingInfo.address,
                                    phone = shippingInfo.phone,
                                    trackingNumber = shippingInfo.trackingNumber,
                                )
                            }
                        }
                        ParticipantStatusType.DELIVERED -> {
                            participant.shippingInfo?.trackingNumber?.let { trackingNumber ->
                                ParticipantDeliveredContent(
                                    trackingNumber = trackingNumber,
                                )
                            }
                        }
                        else -> {}
                    }
                }

                PotiDivider(
                    styleType = PotiDividerStyle.SMALL,
                )
            }
        }
    }
}
