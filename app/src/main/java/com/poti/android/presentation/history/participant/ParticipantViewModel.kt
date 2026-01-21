package com.poti.android.presentation.history.participant

import ParticipantDetailUiModel
import ParticipantShippingUiModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.PaymentInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiEffect
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiIntent
import com.poti.android.presentation.history.participant.model.ParticipantDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipantViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ParticipantDetailUiState, ParticipantDetailUiIntent, ParticipantDetailUiEffect>(
        initialState = ParticipantDetailUiState(),
    ) {
    private val participantId: Long = savedStateHandle.toRoute<HistoryRoute.ParticipantDetail>().participantId

    override fun processIntent(intent: ParticipantDetailUiIntent) {
        when (intent) {
            ParticipantDetailUiIntent.ConfirmDelivery -> TODO()
            is ParticipantDetailUiIntent.LoadDetail -> TODO()
            ParticipantDetailUiIntent.OnBackClick -> TODO()
            ParticipantDetailUiIntent.OnPartyDetailClick -> TODO()
            ParticipantDetailUiIntent.SkipReview -> TODO()
            is ParticipantDetailUiIntent.SubmitDeposit -> TODO()
            is ParticipantDetailUiIntent.SubmitReview -> TODO()
        }
    }

    private fun getParticipantDetail(participantId: Long) = launchScope {
        updateState {
            copy(
                ApiState.Success(
                    ParticipantDetailUiModel(
                        participationId = 1,
                        orderNumber = "참여번호 poti-1",
                        partySummary = PartySummary(
                            imageUrl = "",
                            artist = "아이브(아이브)",
                            title = "러브다이브 위드뮤",
                            partyStatus = PartyStatusType.RECRUITING,
                            statusMessage = "다른 참여자를 기다리고 있어요",
                        ),
                        memberPayments = listOf(
                            MemberPayment(
                                memberName = "멤버1",
                                price = 9000,
                            ),
                        ),
                        paymentInfo = PaymentInfo(
                            shippingFee = 9000,
                            totalAmount = 18000,
                            depositStatus = ParticipantStatusType.RECRUITING,
                            bank = null,
                            accountNumber = null,
                            depositDeadline = null,
                        ),
                        shippingInfo = ParticipantShippingUiModel(
                            shippingMethod = "일반택",
                            addressInfo = "이포티\n(01234) 서울특별시 솝트구 다솝로 456\n010-1234-5678",
                            carrier = null,
                            trackingNumber = null,
                            shippingStatus = ParticipantStatusType.RECRUITING,
                        ),
                    ),
                ),
            )
        }
    }
}
