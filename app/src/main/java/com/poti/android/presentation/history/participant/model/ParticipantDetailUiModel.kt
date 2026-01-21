import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.domain.type.PartyStatusType
import com.poti.android.presentation.history.participant.model.ParticipantButtonState

data class ParticipantDetailUiModel(
    val participationId: Long,
    val orderNumber: String,
    val partySummary: PartySummary,
    val memberPayments: List<MemberPayment>,
    val paymentInfo: PaymentInfoUiModel,
    val shippingInfo: ParticipantShippingUiModel,
    val buttonState: ParticipantButtonState,
)

data class PaymentInfoUiModel(
    val shippingFee: Int,
    val totalAmount: Int,
    val depositStatus: ParticipantStatusType,
    val accountInfo: String,
    val depositDeadline: String?,
)

data class ParticipantShippingUiModel(
    val shippingMethod: String,
    val deliveryTrackingInfo: String,
    val receiver: String,
    val addressInfo: String,
    val carrier: String?,
    val trackingNumber: String?,
    val shippingStatus: ParticipantStatusType,
)

fun ParticipantDetail.toUiModel(): ParticipantDetailUiModel {
    return ParticipantDetailUiModel(
        participationId = this.participationId,
        orderNumber = this.orderNumber,
        partySummary = this.partySummary,
        memberPayments = this.memberPayments,
        paymentInfo = PaymentInfoUiModel(
            shippingFee = this.paymentInfo.shippingFee,
            totalAmount = this.paymentInfo.totalAmount,
            depositStatus = this.paymentInfo.depositStatus,
            accountInfo = if (this.paymentInfo.bank != null && this.paymentInfo.accountNumber != null) {
                "${this.paymentInfo.bank} ${this.paymentInfo.accountNumber} ${this.shippingInfo.receiver}"
            } else {
                "-"
            },
            depositDeadline = this.paymentInfo.depositDeadline,
        ),
        shippingInfo = ParticipantShippingUiModel(
            shippingMethod = this.shippingInfo.shippingMethod,
            deliveryTrackingInfo = if (this.shippingInfo.trackingNumber != null) {
                "${this.shippingInfo.shippingMethod} ${this.shippingInfo.trackingNumber}"
            } else {
                this.shippingInfo.shippingMethod
            },
            receiver = this.shippingInfo.receiver,
            addressInfo = "${this.shippingInfo.receiver}\n(${this.shippingInfo.zipcode}) ${this.shippingInfo.address}\n${this.shippingInfo.phone}",
            carrier = this.shippingInfo.carrier,
            trackingNumber = this.shippingInfo.trackingNumber,
            shippingStatus = this.shippingInfo.shippingStatus,
        ),
        buttonState = when {
            this.partySummary.partyStatus == PartyStatusType.CLOSED && this.paymentInfo.depositStatus == ParticipantStatusType.WAIT_PAY -> {
                ParticipantButtonState.DEPOSIT_DONE
            }
            this.shippingInfo.shippingStatus == ParticipantStatusType.SHIPPED -> {
                ParticipantButtonState.DELIVERY_RECEIVED
            }
            else -> ParticipantButtonState.NONE
        },
    )
}
