import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.PartySummary
import com.poti.android.domain.model.history.PaymentInfo
import com.poti.android.domain.type.ParticipantStatusType

data class ParticipantDetailUiModel(
    val participationId: Long,
    val orderNumber: String,
    val partySummary: PartySummary,
    val memberPayments: List<MemberPayment>,
    val paymentInfo: PaymentInfo,
    val shippingInfo: ParticipantShippingUiModel,
)

data class ParticipantShippingUiModel(
    val shippingMethod: String,
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
        paymentInfo = this.paymentInfo,
        shippingInfo = ParticipantShippingUiModel(
            shippingMethod = this.shippingInfo.shippingMethod,
            addressInfo = "${this.shippingInfo.receiver}\n(${this.shippingInfo.zipcode}) ${this.shippingInfo.address}\n${this.shippingInfo.phone}",
            carrier = this.shippingInfo.carrier,
            trackingNumber = this.shippingInfo.trackingNumber,
            shippingStatus = this.shippingInfo.shippingStatus,
        ),
    )
}
