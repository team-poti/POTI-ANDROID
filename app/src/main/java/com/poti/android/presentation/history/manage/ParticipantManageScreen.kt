package com.poti.android.presentation.history.mapper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.R
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ParticipantManageDetail
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.component.DetailState
import com.poti.android.presentation.history.component.HistoryParticipantDropdown
import com.poti.android.presentation.history.model.manage.RecruiterManageStateUiModel

@Composable
fun ParticipantManageRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit = {},
    navigateToDetail: (Long) -> Unit = {},
) {
    // TODO: [천민재] 실제 데이터 연동 필요
    val participants = remember {
        listOf(
            ParticipantManageDetail(
                participantId = 1,
                nickname = "포티",
                profileImage = null,
                participantState = ParticipantStatusType.DEPOSIT_CHECK,
                selectedMember = "장원영",
                memberPrice = 15000,
                deliveryMethod = "GS반값택배",
                deliveryPrice = 1800,
                depositTime = "2024.12.31 15:30",
                depositorName = "김철수",
                recipient = null,
                phoneNumber = null,
                zipcode = null,
                address = null,
                trackingNumber = null,
            ),
            ParticipantManageDetail(
                participantId = 2,
                nickname = "이영희",
                profileImage = null,
                participantState = ParticipantStatusType.DELIVERY_WAIT,
                selectedMember = "카리나",
                memberPrice = 20000,
                deliveryMethod = "CU끼리택배",
                deliveryPrice = 1600,
                depositTime = null,
                depositorName = null,
                recipient = "이영희",
                phoneNumber = "010-1234-5678",
                zipcode = "12345",
                address = "서울특별시 강남구 역삼동",
                trackingNumber = null,
            ),
        )
    }

    ParticipantManageScreen(
        participants = participants,
        onBackClick = popBackStack,
        modifier = modifier,
    )
}

@Composable
private fun ParticipantManageScreen(
    participants: List<ParticipantManageDetail>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedIds = remember { mutableStateListOf<Long>() }
    val uiModels = remember(participants) { participants.map { it.toUiModel() } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PotiTheme.colors.gray100),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.history_participant_management_title, participants.size),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(uiModels, key = { _, item -> item.participantId }) { index, uiModel ->
                if(index == 0) {
                    PotiDivider(
                        styleType = PotiDividerStyle.SMALL
                    )
                }

                val isExpanded = uiModel.participantId in expandedIds

                val detailState = when (val state = uiModel.detailState) {
                    RecruiterManageStateUiModel.Default -> DetailState.Default
                    is RecruiterManageStateUiModel.DepositCheck -> DetailState.DepositCheck(
                        deposit = state.deposit,
                        onButtonClick = { /* TODO: Handle deposit confirm */ },
                    )
                    is RecruiterManageStateUiModel.Delivery -> DetailState.Delivery(
                        name = state.name,
                        delivery = state.delivery,
                        contact = state.contact,
                        onButtonClick = { /* TODO: Handle delivery */ },
                    )
                    is RecruiterManageStateUiModel.AfterDelivery -> DetailState.AfterDelivery(
                        name = state.name,
                        delivery = state.delivery,
                        contact = state.contact,
                        invoice = state.invoice,
                    )
                    is RecruiterManageStateUiModel.Finished -> DetailState.Finished(
                        invoice = state.invoice,
                    )
                }

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
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun ParticipantManageScreenAllStatesPreview() {
    val participants = listOf(
        // 1. 입금 확인 (Deposit Check)
        // - 입금자명, 입금 시간 정보가 표시되어야 함
        // - [입금 확인] 버튼 활성화 예상
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
        // - 특별한 하단 정보 없음 (Default State)
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
        // - 배송지 정보(이름, 주소, 연락처)가 표시되어야 함
        // - [운송장 입력] 버튼 활성화 예상
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
        // - 배송지 정보 + 운송장 번호가 표시되어야 함
        // - 버튼 없음 (정보 표시 전용)
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
    )

    PotiTheme {
        ParticipantManageScreen(
            participants = participants,
            onBackClick = {},
        )
    }
}
