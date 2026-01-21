package com.poti.android.presentation.history.participant

import ParticipantDetailUiModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.presentation.history.component.HistoryDetailContentHeader
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.participant.component.DeliveryStatusContent
import com.poti.android.presentation.history.participant.component.DepositStatusContent

@Composable
fun ParticipantDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: ParticipantViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.participantDetailState.onSuccess { participantDetail ->
        ParticipantDetailScreen(
            participantDetail = participantDetail,
            onBackClick = {},
            onDetailClick = {},
            modifier = modifier,
        )
    }
}

@Composable
private fun ParticipantDetailScreen(
    participantDetail: ParticipantDetailUiModel,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(id = R.string.history_ongoing_title),
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp),
        ) {
            PartyInfoSection(
                orderNumber = participantDetail.orderNumber,
                partySummary = participantDetail.partySummary,
                onDetailClick = onDetailClick,
                modifier = Modifier.padding(horizontal = screenWidthDp(8.dp)),
            )

            ProgressStatusSection(
                progressStatus = participantDetail.partySummary.partyStatus,
                statusMessage = participantDetail.partySummary.statusMessage,
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
            )

            PotiDivider(
                styleType = PotiDividerStyle.LARGE,
                modifier = Modifier.padding(top = 24.dp),
            )

            HistoryDetailContentHeader(text = stringResource(R.string.history_participant_field_type_deposit))

            DepositStatusContent(
                memberPayments = participantDetail.memberPayments,
                shippingInfo = participantDetail.shippingInfo,
                paymentInfo = participantDetail.paymentInfo,
                participantStatusType = participantDetail.paymentInfo.depositStatus,
            )

            PotiDivider(PotiDividerStyle.LARGE)

            HistoryDetailContentHeader(text = stringResource(R.string.history_shipping_info_title))

            DeliveryStatusContent(
                shippingInfo = participantDetail.shippingInfo,
                participantStatusType = participantDetail.shippingInfo.shippingStatus,
            )
        }
    }
}
