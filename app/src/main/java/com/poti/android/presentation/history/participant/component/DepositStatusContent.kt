package com.poti.android.presentation.history.participant.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.MemberPayment
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor
import com.poti.android.presentation.history.participant.model.ParticipantShippingUiModel
import com.poti.android.presentation.history.participant.model.PaymentInfoUiModel

@Composable
fun DepositStatusContent(
    memberPayments: List<MemberPayment>,
    shippingInfo: ParticipantShippingUiModel,
    paymentInfo: PaymentInfoUiModel,
    participantStatusType: ParticipantStatusType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
            ParticipantStatusType.WAIT_PAY, ParticipantStatusType.WAIT_PAY_CHECK -> {
                DepositInfo(
                    depositInfo = paymentInfo.accountInfo,
                    deadline = paymentInfo.depositDeadline,
                    modifier = Modifier.padding(vertical = 20.dp),
                )

                Text(
                    text = stringResource(participantStatusType.labelResId),
                    color = participantStatusType.statusColor.color,
                    style = PotiTheme.typography.body16sb,
                    modifier = Modifier.align(Alignment.End),
                )
            }
            ParticipantStatusType.PAID, ParticipantStatusType.READY, ParticipantStatusType.SHIPPED, ParticipantStatusType.DELIVERED -> {
                Text(
                    text = stringResource(participantStatusType.labelResId),
                    color = participantStatusType.statusColor.color,
                    style = PotiTheme.typography.body16sb,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.End),
                )
            }
        }
    }
}

@Composable
fun DepositInfo(
    depositInfo: String,
    deadline: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HistoryCalloutInfo(
            text = depositInfo,
            copyable = true,
        )
        deadline?.let {
            HistoryCalloutInfo(
                text = deadline,
                copyable = false,
            )
        }
    }
}
