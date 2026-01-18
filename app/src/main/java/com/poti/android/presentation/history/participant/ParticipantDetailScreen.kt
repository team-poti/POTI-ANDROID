package com.poti.android.presentation.history.participant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.domain.model.history.DepositStatus
import com.poti.android.domain.model.history.ParticipantDepositInfo
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.HistoryCalloutInfo
import com.poti.android.presentation.history.component.HistoryDepositBottomSheet
import com.poti.android.presentation.history.component.HistoryParticipantStateLabel
import com.poti.android.presentation.history.component.ParticipantStateLabelSize
import com.poti.android.presentation.history.component.ParticipantStateLabelStage
import com.poti.android.presentation.history.component.ParticipantStateLabelStatus
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.mapper.toUiState

private sealed interface ParticipantDetailModalState {
    data object None : ParticipantDetailModalState

    data object DepositInput : ParticipantDetailModalState

    data object DeliveryConfirm : ParticipantDetailModalState
}

@Composable
fun ParticipantDetailRoute(
    onBackClick: () -> Unit,
    onNavigateToPartyDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ParticipantDetailScreen(
        modifier = modifier,
        detail = DummyParticipantManageDetail.participantDetailWaitDeposit,
        onDetailClick = onNavigateToPartyDetail,
        onBackClick = onBackClick,
        // ViewModel 에서 처리
        onDepositSubmit = { i, j -> },
    )
}

@Composable
private fun ParticipantDetailScreen(
    detail: ParticipantDetail,
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit,
    onDepositSubmit: (depositor: String, depositTime: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var modalState by remember { mutableStateOf<ParticipantDetailModalState>(ParticipantDetailModalState.None) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(id = R.string.history_participant_detail_title),
                modifier = Modifier.padding(top = 16.dp),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PotiTheme.colors.white),
        ) {
            item {
                PartyInfoSection(
                    partyId = detail.partyId,
                    artistInfo = detail.artistInfo,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                ProgressStatusSection(
                    progressInfo = detail.progressInfo,
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
                )
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                    modifier = Modifier.padding(vertical = 20.dp),
                )
            }

            item {
                DepositInfoSection(info = detail.depositInfo)
            }

            item {
                PotiDivider(
                    styleType = PotiDividerStyle.LARGE,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }

            item {
                ShippingInfoSection(
                    info = detail.shippingInfo,
                    modifier = Modifier.padding(top = 20.dp),
                )

                val (stage, state) = detail.userState.toUiState()

                if (stage == ParticipantStateLabelStage.DELIVERY) {
                    if (state == ParticipantStateLabelStatus.START) {
                        HistoryCalloutInfo(
                            text = detail.depositInfo.depositStatus.accountNumber,
                            copyable = true,
                            modifier = Modifier.padding(top = 20.dp),
                        )
                    }

                    Spacer(
                        Modifier.height(
                            if (state == ParticipantStateLabelStatus.START) {
                                12.dp
                            } else {
                                20.dp
                            },
                        ),
                    )

                    HistoryParticipantStateLabel(
                        sizeType = ParticipantStateLabelSize.LARGE,
                        stageType = stage,
                        statusType = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End),
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            if (detail.userState == ParticipantStatusType.RECRUIT_DONE ||
                detail.userState == ParticipantStatusType.DELIVERY_START
            ) {
                item {
                    PotiActionButton(
                        text = if (detail.userState == ParticipantStatusType.RECRUIT_DONE) {
                            stringResource(R.string.history_deposit_done_button)
                        } else {
                            stringResource(R.string.history_delivery_done_button)
                        },
                        onClick = {
                            modalState = when (detail.userState) {
                                ParticipantStatusType.RECRUIT_DONE -> ParticipantDetailModalState.DepositInput
                                ParticipantStatusType.DELIVERY_START -> ParticipantDetailModalState.DeliveryConfirm
                                else -> ParticipantDetailModalState.None
                            }
                        },
                        type = ActionButtonType.SECONDARY_MAIN,
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
    when (modalState) {
        ParticipantDetailModalState.None -> Unit

        ParticipantDetailModalState.DepositInput -> {
            HistoryDepositBottomSheet(
                onDismissRequest = { modalState = ParticipantDetailModalState.None },
                onConfirmClick = { depositor, time ->
                    modalState = ParticipantDetailModalState.None
                    onDepositSubmit(depositor, time)
                },
            )
        }

        ParticipantDetailModalState.DeliveryConfirm -> {
        }
    }
}

@Composable
private fun DepositInfoSection(
    info: ParticipantDepositInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(16.dp)),
    ) {
        Text(
            text = stringResource(id = R.string.history_deposit_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
            modifier = Modifier.padding(bottom = 20.dp),
        )

        PriceDetail(items = info.items, totalAmount = info.totalAmount)

        val depositStatus = info.depositStatus
        if (depositStatus is DepositStatus.DepositCheck ||
            depositStatus is DepositStatus.DepositWait
        ) {
            HistoryCalloutInfo(
                text = depositStatus.accountNumber,
                copyable = true,
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
            )

            HistoryCalloutInfo(
                text = depositStatus.dueDate,
                copyable = false,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            val (stage, status) = depositStatus.toUiState()

            HistoryParticipantStateLabel(
                sizeType = ParticipantStateLabelSize.LARGE,
                stageType = stage,
                statusType = status,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Composable
private fun PriceDetail(
    items: List<DepositItem>,
    totalAmount: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            val optionType = item.toUiState()
            PotiListOptionPrice(
                itemOptionType = optionType,
                itemOptionText = item.name,
                priceText = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    item.price.toMoneyString(),
                ),
                sizeType = PotiListOptionPriceSize.SMALL,
            )
        }

        PotiDivider(styleType = PotiDividerStyle.SMALL)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.history_total_deposit_amount),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
            )

            Text(
                text = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    totalAmount.toMoneyString(),
                ),
                style = PotiTheme.typography.body16sb,
                color = PotiTheme.colors.black,
            )
        }
    }
}

@Composable
private fun ShippingInfoSection(
    info: ParticipantShippingInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.history_shipping_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(
                    R.string.history_shipping_info_format,
                    info.recipient,
                    info.zipcode,
                    info.address,
                    info.phone,
                ),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.black,
                lineHeight = PotiTheme.typography.body14m.fontSize * 1.5,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delivery),
                    contentDescription = "Delivery Method",
                    tint = PotiTheme.colors.gray800,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = info.deliveryMethod,
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800,
                )
            }
        }
    }
}

class ParticipantDetailPreviewProvider : PreviewParameterProvider<ParticipantDetail> {
    override val values: Sequence<ParticipantDetail> = sequenceOf(
        DummyParticipantManageDetail.participantDetailWaitDeposit, // 1. 입금 대기 (버튼: 입금 완료)
        DummyParticipantManageDetail.participantDetailCheckDeposit, // 2. 입금 확인 (버튼 없음)
        DummyParticipantManageDetail.participantDetailDeliveryStart, // 3. 배송 시작 (버튼: 수령 완료)
    )
}

@Preview(showBackground = true)
@Composable
private fun ParticipantDetailScreenPreview(
    @PreviewParameter(ParticipantDetailPreviewProvider::class) detail: ParticipantDetail,
) {
    PotiTheme {
        ParticipantDetailScreen(
            detail = detail,
            onBackClick = {},
            onDetailClick = {},
            onDepositSubmit = { _, _ -> },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}
