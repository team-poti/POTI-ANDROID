package com.poti.android.presentation.history.manage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.DetailState
import com.poti.android.presentation.history.component.HistoryDeliveryBottomSheet
import com.poti.android.presentation.history.component.HistoryDepositConfirmModal
import com.poti.android.presentation.history.component.HistoryParticipantDropdown
import com.poti.android.presentation.history.mapper.toUiModel
import com.poti.android.presentation.history.model.manage.ManageModalState
import com.poti.android.presentation.history.model.manage.ParticipantManageUiEffect
import com.poti.android.presentation.history.model.manage.ParticipantManageUiIntent
import com.poti.android.presentation.history.model.manage.RecruiterManageDetailUiModel
import com.poti.android.presentation.history.model.manage.RecruiterManageStateUiModel

@Composable
fun ParticipantManageRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    viewModel: ParticipantManageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ParticipantManageUiEffect.NavigateBack -> popBackStack()
            }
        }
    }

    when (val state = uiState.participantManageDetail) {
        is ApiState.Success -> {
            ParticipantManageScreen(
                uiModels = state.data,
                activeModal = uiState.activeModal,
                onBackClick = { viewModel.processIntent(ParticipantManageUiIntent.OnBackClick) },
                onDepositConfirmClick = { id -> viewModel.processIntent(ParticipantManageUiIntent.OnDepositConfirmClick(id)) },
                onDeliveryInputClick = { id -> viewModel.processIntent(ParticipantManageUiIntent.OnDeliveryInputClick(id)) },
                onDismissModal = { viewModel.processIntent(ParticipantManageUiIntent.DismissModal) },
                onDepositModalConfirm = { id ->
                    viewModel.processIntent(ParticipantManageUiIntent.ConfirmDeposit(participantId = id))
                },
                onDeliveryModalConfirm = { participantId, deliveryMethod, trackingNumber ->
                    viewModel.processIntent(
                        ParticipantManageUiIntent.RegisterDelivery(
                            participantId = participantId,
                            deliveryMethod = deliveryMethod,
                            trackingNumber = trackingNumber,
                        ),
                    )
                },
                modifier = modifier,
            )
        }
        else -> Unit
    }
}

@Composable
private fun ParticipantManageScreen(
    uiModels: List<RecruiterManageDetailUiModel>,
    activeModal: ManageModalState,
    onBackClick: () -> Unit,
    onDepositConfirmClick: (Long) -> Unit,
    onDeliveryInputClick: (Long) -> Unit,
    onDismissModal: () -> Unit,
    onDepositModalConfirm: (Long) -> Unit,
    onDeliveryModalConfirm: (participantId: Long, deliveryMethod: String, trackingNumber: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedIds = remember(uiModels) { mutableStateListOf<Long>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PotiTheme.colors.gray100),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.history_participant_management_title),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .animateContentSize(),
        ) {
            uiModels.forEachIndexed { index, uiModel ->
                if (index == 0) {
                    PotiDivider(
                        styleType = PotiDividerStyle.SMALL,
                    )
                }

                val isExpanded = uiModel.participantId in expandedIds

                val detailState = uiModel.detailState.toDetailState(
                    onDepositConfirm = { onDepositConfirmClick(uiModel.participantId) },
                    onDeliveryInput = { onDeliveryInputClick(uiModel.participantId) },
                )

                HistoryParticipantDropdown(
                    userName = uiModel.nickname,
                    userImageUrl = uiModel.profileImage,
                    depositItems = uiModel.depositItems,
                    depositTotalPrice = uiModel.depositTotalPrice,
                    detailState = detailState,
                    stageType = uiModel.stage,
                    statusType = uiModel.status,
                    isExpanded = isExpanded,
                    onToggle = {
                        if (isExpanded) {
                            expandedIds.remove(uiModel.participantId)
                        } else {
                            expandedIds.add(uiModel.participantId)
                        }
                    },
                )

                PotiDivider(
                    styleType = PotiDividerStyle.SMALL,
                )
            }
        }
    }

    when (activeModal) {
        is ManageModalState.DepositConfirm -> {
            HistoryDepositConfirmModal(
                onConfirm = { onDepositModalConfirm(activeModal.participantId) },
                onDismiss = onDismissModal,
            )
        }
        is ManageModalState.DeliveryInput -> {
            HistoryDeliveryBottomSheet(
                onDismissRequest = onDismissModal,
                onConfirmClick = { deliveryMethod, trackingNumber ->
                    onDeliveryModalConfirm(
                        // participantId =
                        activeModal.participantId,
                        // deliveryMethod =
                        deliveryMethod,
                        // trackingNumber =
                        trackingNumber,
                    )
                },
            )
        }
        ManageModalState.None -> Unit
    }
}

private fun RecruiterManageStateUiModel.toDetailState(
    onDepositConfirm: () -> Unit,
    onDeliveryInput: () -> Unit,
): DetailState {
    return when (this) {
        RecruiterManageStateUiModel.Default -> DetailState.Default
        is RecruiterManageStateUiModel.DepositCheck -> DetailState.DepositCheck(
            deposit = this.deposit,
            onButtonClick = onDepositConfirm,
        )
        is RecruiterManageStateUiModel.Delivery -> DetailState.Delivery(
            name = this.name,
            delivery = this.delivery,
            contact = this.contact,
            onButtonClick = onDeliveryInput,
        )
        is RecruiterManageStateUiModel.AfterDelivery -> DetailState.AfterDelivery(
            name = this.name,
            delivery = this.delivery,
            contact = this.contact,
            invoice = this.invoice,
        )
        is RecruiterManageStateUiModel.Finished -> DetailState.Finished(
            invoice = this.invoice,
        )
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun ParticipantManageScreenAllStatesPreview() {
    val participants = listOf(
        // 1. 입금 확인 (Deposit Check)
        ParticipantManageDetail(
            participantId = 1,
            nickname = "포티_입금확인중",
            profileImage = null,
            participantState = ParticipantStatusType.DEPOSIT_CHECK,
            selectedMember = "해린",
            memberPrice = 15000,
            deliveryMethod = "GS반값택배",
            deliveryPrice = 1800,
            depositTime = "2024.12.31 15:30",
            depositorName = "김입금",
            recipient = null,
            phoneNumber = null,
            zipcode = null,
            address = null,
            trackingNumber = null,
        ),
        // 2. 입금 완료 (Deposit Done)
        ParticipantManageDetail(
            participantId = 2,
            nickname = "포티_입금완료",
            profileImage = null,
            participantState = ParticipantStatusType.DEPOSIT_DONE,
            selectedMember = "민지",
            memberPrice = 15000,
            deliveryMethod = "준등기",
            deliveryPrice = 1800,
            depositTime = null,
            depositorName = null,
            recipient = null,
            phoneNumber = null,
            zipcode = null,
            address = null,
            trackingNumber = null,
        ),
        // 3. 배송 대기 (Delivery Wait)
        ParticipantManageDetail(
            participantId = 3,
            nickname = "포티_배송대기",
            profileImage = null,
            participantState = ParticipantStatusType.DELIVERY_WAIT,
            selectedMember = "카리나",
            memberPrice = 20000,
            deliveryMethod = "CU끼리택배",
            deliveryPrice = 1600,
            depositTime = null,
            depositorName = null,
            recipient = "이수령",
            phoneNumber = "010-1234-5678",
            zipcode = "12345",
            address = "서울특별시 강남구 테헤란로 123 포티타워",
            trackingNumber = null,
        ),
        // 4. 배송 중/완료 (Delivery Done/Start)
        ParticipantManageDetail(
            participantId = 4,
            nickname = "포티_배송완료",
            profileImage = null,
            participantState = ParticipantStatusType.DELIVERY_DONE,
            selectedMember = "안유진",
            memberPrice = 18000,
            deliveryMethod = "일반택배",
            deliveryPrice = 3500,
            depositTime = null,
            depositorName = null,
            recipient = "박완료",
            phoneNumber = "010-9876-5432",
            zipcode = "54321",
            address = "경기도 성남시 분당구 판교로 999",
            trackingNumber = "1234-5678-9012",
        ),
    ).map { it.toUiModel() }

    var fakeModalState by remember { mutableStateOf<ManageModalState>(ManageModalState.None) }

    PotiTheme {
        Scaffold { innerPadding ->
            ParticipantManageScreen(
                uiModels = participants,
                activeModal = fakeModalState,
                onBackClick = {},
                onDepositConfirmClick = { id ->
                    fakeModalState = ManageModalState.DepositConfirm(id)
                },
                onDeliveryInputClick = { id ->
                    fakeModalState = ManageModalState.DeliveryInput(id)
                },
                onDismissModal = {
                    fakeModalState = ManageModalState.None
                },
                onDepositModalConfirm = {
                    fakeModalState = ManageModalState.None
                },
                onDeliveryModalConfirm = { _, _, _ ->
                    fakeModalState = ManageModalState.None
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}
