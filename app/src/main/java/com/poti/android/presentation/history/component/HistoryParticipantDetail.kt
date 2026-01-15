package com.poti.android.presentation.history.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiInlineButton
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography
import java.util.Locale

// TODO [천민재] 맵핑 확장함수 추후 구현
data class DepositItem(
    val type: PotiItemOptionType,
    val name: String,
    val price: Int,
)

sealed interface DetailState {
    val fields: List<Pair<FieldType, String>>
    val onClickLabelId: Int?
    val onConfirmClick: (() -> Unit)?

    data object Default : DetailState {
        override val fields: List<Pair<FieldType, String>> = emptyList()
        override val onClickLabelId: Int? = null
        override val onConfirmClick: (() -> Unit)? = null
    }

    data class DepositCheck(
        val deposit: String,
        override val onConfirmClick: () -> Unit,
    ) : DetailState {
        override val fields = listOf(
            FieldType.DEPOSIT to deposit,
        )
        override val onClickLabelId: Int =
            R.string.history_participant_field_deposit_label
    }

    data class Delivery(
        val name: String,
        val delivery: String,
        val contact: String,
        override val onConfirmClick: () -> Unit,
    ) : DetailState {
        override val fields = listOf(
            FieldType.NAME to name,
            FieldType.DELIVERY to delivery,
            FieldType.CONTACT to contact,
        )
        override val onClickLabelId: Int =
            R.string.history_participant_field_delivery_label
    }

    data class AfterDelivery(
        val name: String,
        val delivery: String,
        val contact: String,
        val invoice: String,
    ) : DetailState {
        override val fields = listOf(
            FieldType.NAME to name,
            FieldType.DELIVERY to delivery,
            FieldType.CONTACT to contact,
            FieldType.INVOICE to invoice,
        )
        override val onConfirmClick: (() -> Unit)? = null
        override val onClickLabelId: Int? = null
    }

    data class Finished(
        val invoice: String,
    ) : DetailState {
        override val fields = listOf(
            FieldType.INVOICE to invoice,
        )
        override val onConfirmClick: (() -> Unit)? = null
        override val onClickLabelId: Int? = null
    }
}

enum class FieldType(
    @StringRes val labelId: Int,
) {
    NAME(R.string.history_participant_field_type_name),
    DEPOSIT(R.string.history_participant_field_type_deposit),
    DELIVERY(R.string.history_participant_field_type_delivery),
    CONTACT(R.string.history_participant_field_type_contact),
    INVOICE(R.string.history_participant_field_type_invoice),
}

@Composable
fun HistoryParticipantDetail(
    userName: String,
    userImageUrl: String,
    depositItems: List<DepositItem>,
    detailState: DetailState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.gray100)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = userImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(99.dp)),
            )

            Text(
                text = userName,
                style = typography.body14m,
                color = colors.black,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp),
            )
        }

        PotiDivider(
            styleType = PotiDividerStyle.SMALL,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "입금 금액",
                style = typography.body14sb,
                color = colors.black,
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                depositItems.forEach { item ->
                    PotiListOptionPrice(
                        itemOptionType = item.type,
                        itemOptionText = item.name,
                        priceText = priceText(item.price),
                        sizeType = PotiListOptionPriceSize.SMALL,
                    )
                }

                PotiDivider(
                    styleType = PotiDividerStyle.SMALL,
                    modifier = Modifier.fillMaxWidth(),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PotiItemOption(
                        optionType = PotiItemOptionType.PRICE,
                        sizeType = PotiItemOptionSize.SMALL,
                        text = "총 입금 금액",
                    )

                    Text(
                        text = priceText(depositItems.sumOf { it.price }),
                        style = typography.body16sb,
                        color = colors.black,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            detailState.fields.forEach { (field, value) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(field.labelId),
                        style = typography.body14sb,
                        color = colors.black,
                    )
                    Text(
                        text = value,
                        style = typography.body14sb,
                        color = colors.black,
                    )
                }
            }
        }
        if (detailState.onConfirmClick != null && detailState.onClickLabelId != null) {
            PotiInlineButton(
                text = stringResource(detailState.onClickLabelId!!),
                onClick = detailState.onConfirmClick!!,
                showIcon = false,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

// TODO: [천민재] 임시 함수
private fun priceText(price: Int) = String.format(
    locale = Locale.KOREA,
    format = "%,d원",
    price,
)

@Preview(showBackground = true)
@Composable
private fun HistoryParticipantDetailPreview() {
    val depositItems = listOf(
        DepositItem(
            type = PotiItemOptionType.PRICE,
            name = "멤버1",
            price = 2000000000,
        ),
        DepositItem(
            type = PotiItemOptionType.PRICE,
            name = "멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2",
            price = 10000,
        ),
        DepositItem(
            type = PotiItemOptionType.PRICE,
            name = "멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2",
            price = 320000000,
        ),
        DepositItem(
            type = PotiItemOptionType.PRICE,
            name = "멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2멤버2",
            price = 320000000,
        ),
        DepositItem(
            type = PotiItemOptionType.DELIVERY,
            name = "등기등기등기등기둥기둥",
            price = 320000000,
        ),
    )

    PotiTheme {
        HistoryParticipantDetail(
            userName = "닉네임",
            userImageUrl = "",
            depositItems = depositItems,
            detailState = DetailState.AfterDelivery(
                name = "이포티",
                delivery = "(01234) 서울특별시 솝트구 다솝로 456",
                contact = "010-1111-1111",
                invoice = "우체국 37249720348093",
            ),
            modifier = Modifier.width(311.dp),
        )
    }
}
