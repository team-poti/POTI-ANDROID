package com.poti.android.presentation.history.manage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.history.DepositInfo
import com.poti.android.domain.model.history.MemberPriceInfo
import com.poti.android.domain.model.history.ShippingInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.manage.model.ManageModalState
import com.poti.android.presentation.history.manage.model.ParticipantManageUiEffect
import com.poti.android.presentation.history.manage.model.ParticipantManageUiIntent
import com.poti.android.presentation.history.manage.model.ParticipantManageUiState
import com.poti.android.presentation.history.manage.model.ParticipantUiModel
import com.poti.android.presentation.history.manage.model.RecruiterManageDetailUiModel
import com.poti.android.presentation.history.navigation.HistoryRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantManageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<ParticipantManageUiState, ParticipantManageUiIntent, ParticipantManageUiEffect>(
        initialState = ParticipantManageUiState(),
    ) {
    private val recruitId: Long = savedStateHandle.toRoute<HistoryRoute.ParticipantManage>().recruitId

    init {
        loadParticipantManageDetail()
    }

    override fun processIntent(intent: ParticipantManageUiIntent) {
        when (intent) {
            ParticipantManageUiIntent.OnBackClick -> sendEffect(ParticipantManageUiEffect.NavigateBack)
            is ParticipantManageUiIntent.OnDepositConfirmClick -> updateState {
                copy(activeModal = ManageModalState.DepositConfirm(intent.participantId))
            }
            is ParticipantManageUiIntent.OnDeliveryInputClick -> updateState {
                copy(activeModal = ManageModalState.DeliveryInput(intent.participantId))
            }
            ParticipantManageUiIntent.OnDepositModalDismiss -> updateState { copy(activeModal = ManageModalState.None) }
            is ParticipantManageUiIntent.OnDepositModalConfirm -> confirmDeposit(intent.participantId)
            is ParticipantManageUiIntent.RegisterDelivery -> registerDelivery(intent.participantId, intent.deliveryMethod, intent.trackingNumber)
        }
    }

    private fun loadParticipantManageDetail() {
        launchScope(
            onError = { throwable ->
                updateState {
                    copy(
                        participantManageDetailLoadState = ApiState.Failure(throwable.message ?: "Unknown Error"),
                    )
                }
            },
        ) {
            updateState { copy(participantManageDetailLoadState = ApiState.Loading) }

            // TODO: [천민재] 실제 API 연동 필요 (현재 더미 데이터 사용)
            val dummyData = RecruiterManageDetailUiModel(
                participants = listOf(
                    ParticipantUiModel(
                        userId = 1,
                        nickname = "포티",
                        profileImage = null,
                        participantStatus = ParticipantStatusType.RECRUITING,
                        memberNames = "멤버1, 멤버2",
                        priceInfo = listOf(
                            MemberPriceInfo(
                                name = "멤버1",
                                price = 5000,
                            ),
                            MemberPriceInfo(
                                name = "멤버2",
                                price = 6000,
                            ),
                        ),
                        shippingName = "준등기",
                        shippingPrice = 1800,
                        totalPrice = 12800,
                        depositInfo = null,
                        shippingInfo = null,
                    ),
                    ParticipantUiModel(
                        userId = 2,
                        nickname = "이영희",
                        profileImage = null,
                        participantStatus = ParticipantStatusType.WAIT_PAY_CHECK,
                        memberNames = "멤버1, 멤버2",
                        priceInfo = listOf(
                            MemberPriceInfo(
                                name = "멤버1",
                                price = 5000,
                            ),
                            MemberPriceInfo(
                                name = "멤버2",
                                price = 6000,
                            ),
                        ),
                        shippingName = "준등기",
                        shippingPrice = 1800,
                        totalPrice = 12800,
                        depositInfo = DepositInfo(
                            depositorName = "이름",
                            depositTime = "2025-12-30 2:50",
                        ),
                        shippingInfo = null,
                    ),
                    ParticipantUiModel(
                        userId = 3,
                        nickname = "이영희",
                        profileImage = null,
                        participantStatus = ParticipantStatusType.PAID,
                        memberNames = "멤버1, 멤버2",
                        priceInfo = listOf(
                            MemberPriceInfo(
                                name = "멤버1",
                                price = 5000,
                            ),
                            MemberPriceInfo(
                                name = "멤버2",
                                price = 6000,
                            ),
                        ),
                        shippingName = "준등기",
                        shippingPrice = 1800,
                        totalPrice = 12800,
                        depositInfo = DepositInfo(
                            depositorName = "이름",
                            depositTime = "2025-12-30 2:50",
                        ),
                        shippingInfo = ShippingInfo(
                            receiverName = "이름",
                            address = "(01234) 서울특별시 솝트구 다솝로 456",
                            phone = "010-1234-5678",
                            trackingNumber = null,
                        ),
                    ),
                    ParticipantUiModel(
                        userId = 4,
                        nickname = "닉네임",
                        profileImage = null,
                        participantStatus = ParticipantStatusType.PAID,
                        memberNames = "멤버1, 멤버2",
                        priceInfo = listOf(
                            MemberPriceInfo(
                                name = "멤버1",
                                price = 5000,
                            ),
                            MemberPriceInfo(
                                name = "멤버2",
                                price = 6000,
                            ),
                        ),
                        shippingName = "준등기",
                        shippingPrice = 1800,
                        totalPrice = 12800,
                        depositInfo = DepositInfo(
                            depositorName = "이름",
                            depositTime = "2025-12-30 2:50",
                        ),
                        shippingInfo = ShippingInfo(
                            receiverName = "이름",
                            address = "(01234) 서울특별시 솝트구 다솝로 456",
                            phone = "010-1234-5678",
                            trackingNumber = "우체국 345567788653221",
                        ),
                    ),
                    ParticipantUiModel(
                        userId = 5,
                        nickname = "이영희",
                        profileImage = null,
                        participantStatus = ParticipantStatusType.DELIVERED,
                        memberNames = "멤버1, 멤버2",
                        priceInfo = listOf(
                            MemberPriceInfo(
                                name = "멤버1",
                                price = 5000,
                            ),
                            MemberPriceInfo(
                                name = "멤버2",
                                price = 6000,
                            ),
                        ),
                        shippingName = "준등기",
                        shippingPrice = 1800,
                        totalPrice = 12800,
                        depositInfo = DepositInfo(
                            depositorName = "이름",
                            depositTime = "2025-12-30 2:50",
                        ),
                        shippingInfo = ShippingInfo(
                            receiverName = "이름",
                            address = "(01234) 서울특별시 솝트구 다솝로 456",
                            phone = "010-1234-5678",
                            trackingNumber = "우체국 345567788653221",
                        ),
                    ),
                ),
            )

            updateState {
                copy(participantManageDetailLoadState = ApiState.Success(dummyData))
            }
        }
    }

    private fun confirmDeposit(id: Long) {
        viewModelScope.launch {
            updateState { copy(activeModal = ManageModalState.None) }

            // TODO: [천민재] API 전달
        }
    }

    private fun registerDelivery(
        id: Long,
        method: String,
        number: String,
    ) {
        viewModelScope.launch {
            updateState { copy(activeModal = ManageModalState.None) }

            // TODO: [천민재] API 전달
        }
    }
}
