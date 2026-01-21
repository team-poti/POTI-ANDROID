package com.poti.android.presentation.history.participant.component

import ParticipantShippingUiModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.model.history.PaymentInfo
import com.poti.android.domain.type.ParticipantStatusType

@Composable
fun DepositStatusContent(
    memberPayments: List<MemberPayment>,
    shippingInfo: ParticipantShippingUiModel,
    paymentInfo: PaymentInfo,
    participantStatusType: ParticipantStatusType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = screenWidthDp(16.dp))
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        memberPayments.forEach { member ->
            PotiListOptionPrice(
                itemOptionType = PotiItemOptionType.MEMBER,
                itemOptionText = member.memberName,
                priceText = stringResource(R.string.party_option_price_won, member.price.toMoneyString()),
                sizeType = PotiListOptionPriceSize.SMALL,
            )
        }

        PotiListOptionPrice(
            itemOptionType = PotiItemOptionType.DELIVERY,
            itemOptionText = shippingInfo.shippingMethod,
            priceText = stringResource(R.string.party_option_price_won, paymentInfo.shippingFee.toMoneyString()),
            sizeType = PotiListOptionPriceSize.SMALL,
        )

        PotiDivider(PotiDividerStyle.SMALL)

        PotiListOptionPrice(
            itemOptionType = PotiItemOptionType.PRICE,
            itemOptionText = stringResource(R.string.history_participant_detail_total_deposit_label),
            priceText = stringResource(R.string.party_option_price_won, paymentInfo.totalAmount.toMoneyString()),
            sizeType = PotiListOptionPriceSize.LARGE,
        )

        when (participantStatusType) {
            ParticipantStatusType.RECRUITING -> {}
            ParticipantStatusType.WAIT_PAY -> {}
            ParticipantStatusType.WAIT_PAY_CHECK -> {}
            ParticipantStatusType.PAID -> {}
            ParticipantStatusType.READY -> {}
            ParticipantStatusType.SHIPPED -> {}
            ParticipantStatusType.DELIVERED -> {}
        }
    }
}
