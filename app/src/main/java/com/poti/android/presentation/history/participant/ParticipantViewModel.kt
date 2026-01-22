package com.poti.android.presentation.history.participant

import ParticipantDetailUiModel
import ParticipantShippingUiModel
import PaymentInfoUiModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.history.participant.model.ParticipantButtonState
import com.poti.android.presentation.history.participant.model.ParticipantDetailOverlayState
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

    init {
        getParticipantDetail(participantId)
    }

    override fun processIntent(intent: ParticipantDetailUiIntent) {
        when (intent) {
            is ParticipantDetailUiIntent.LoadDetail -> getParticipantDetail(intent.recruitId)
            ParticipantDetailUiIntent.OnBackClick -> sendEffect(ParticipantDetailUiEffect.NavigateBack)
            is ParticipantDetailUiIntent.OnPartyDetailClick ->
                sendEffect(ParticipantDetailUiEffect.NavigateToPartyDetail(intent.partyId))

            ParticipantDetailUiIntent.OnDepositCompleteClick -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.DepositBottomSheet) }
            }

            ParticipantDetailUiIntent.CloseOverlay -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }

            is ParticipantDetailUiIntent.SubmitDeposit -> patchSubmitDeposit(intent.depositor, intent.depositTime)

            ParticipantDetailUiIntent.OnDeliveredClick -> {
                updateState { copy(overlayState = ParticipantDetailOverlayState.DeliveryConfirmModal) }
            }

            ParticipantDetailUiIntent.ConfirmDelivery -> {
                // TODO: 배송 수령 확인 API 호출 및 주최자 정보 획득
                // 성공 시 리뷰 모달로 전환하며 데이터 전달
                updateState {
                    copy(
                        overlayState = ParticipantDetailOverlayState.DeliveryReviewModal(
                            recruiterName = "장원영", // 더미 데이터
                            recruiterProfileUrl = "", // 더미 데이터
                            partnerRating = "4.5", // 더미 데이터
                        ),
                    )
                }
            }

            ParticipantDetailUiIntent.SkipReview -> {
                // TODO: 리뷰 건너뛰기 처리 (필요시 API 호출)
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }

            is ParticipantDetailUiIntent.SubmitReview -> {
                // TODO: 리뷰 제출 API 호출
                updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
            }
        }
    }

    private fun getParticipantDetail(participantId: Long) = launchScope {
        updateState {
            copy(
                participantDetailState = ApiState.Success(
                    ParticipantDetailUiModel(
                        participationId = 1,
                        partyId = 1,
                        orderNumber = "참여번호 poti-1",
                        partySummary = PartySummary(
                            imageUrl = "",
                            artist = "아이브(아이브)",
                            title = "러브다이브 위드뮤",
                            partyStatus = PartyStatusType.CLOSED,
                            statusMessage = "다른 참여자를 기다리고 있어요",
                        ),
                        memberPayments = listOf(
                            MemberPayment(
                                memberName = "멤버1",
                                price = 9000,
                            ),
                        ),
                        paymentInfo = PaymentInfoUiModel(
                            shippingFee = 9000,
                            totalAmount = 18000,
                            depositStatus = ParticipantStatusType.SHIPPED,
                            accountInfo = "카카오뱅크 3333-19-1234123 이포티",
                            depositDeadline = "2026-01-01 23:50 까지",
                        ),
                        shippingInfo = ParticipantShippingUiModel(
                            shippingMethod = "일반택",
                            deliveryTrackingInfo = "우체국 20203344656423232",
                            receiver = "이포티",
                            addressInfo = "이포티\n(01234) 서울특별시 솝트구 다솝로 456\n010-1234-5678",
                            carrier = null,
                            trackingNumber = null,
                            shippingStatus = ParticipantStatusType.SHIPPED,
                        ),
                        buttonState = ParticipantButtonState.DEPOSIT_DONE,
                    ),
                ),
            )
        }
    }

    private fun patchSubmitDeposit(depositor: String, depositTime: String) = launchScope {
        // TODO: 입금 확인 요청 API 호출
        // 성공 시 overlayState = None 및 데이터 갱신
        updateState { copy(overlayState = ParticipantDetailOverlayState.None) }
    }
}
