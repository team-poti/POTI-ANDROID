package com.poti.android.presentation.history.manage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOption
import com.poti.android.core.designsystem.component.display.PotiItemOptionSize
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.history.manage.model.ParticipantUiModel

@Composable
fun HistoryParticipantDetail(
    participant: ParticipantUiModel,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.gray100)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = participant.profileImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
            )

            Text(
                text = participant.nickname,
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f),
            )
        }

        PotiDivider(
            styleType = PotiDividerStyle.SMALL,
            modifier = Modifier.padding(vertical = 16.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.history_participant_detail_deposit_label),
                style = PotiTheme.typography.body14sb,
                color = PotiTheme.colors.black,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                participant.priceInfo.forEach { item ->
                    PotiListOptionPrice(
                        itemOptionType = PotiItemOptionType.MEMBER,
                        itemOptionText = item.name,
                        priceText = stringResource(
                            R.string.history_participant_detail_won_unit_format,
                            item.price.toMoneyString(),
                        ),
                        sizeType = PotiListOptionPriceSize.SMALL,
                    )
                }

                PotiListOptionPrice(
                    itemOptionType = PotiItemOptionType.DELIVERY,
                    itemOptionText = participant.shippingName,
                    priceText = stringResource(
                        R.string.history_participant_detail_won_unit_format,
                        participant.shippingPrice,
                    ),
                    sizeType = PotiListOptionPriceSize.SMALL,
                )

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
                        text = stringResource(R.string.history_participant_detail_total_deposit_label),
                    )

                    Text(
                        text = stringResource(
                            R.string.history_participant_detail_won_unit_format,
                            participant.totalPrice.toMoneyString(),
                        ),
                        style = PotiTheme.typography.body16sb,
                        color = PotiTheme.colors.black,
                    )
                }
            }
        }
        content?.invoke()
    }
}
