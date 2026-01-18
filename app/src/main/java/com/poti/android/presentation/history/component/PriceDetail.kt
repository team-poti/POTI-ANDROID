package com.poti.android.presentation.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.presentation.history.mapper.toUiState

@Composable
fun PriceDetail(
    items: List<DepositItem>,
    totalAmount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            val optionType = item.toUiState()
            PotiListOptionPrice(
                itemOptionType = optionType,
                itemOptionText = item.name,
                priceText = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    item.price.toMoneyString(),
                ),
                sizeType = PotiListOptionPriceSize.SMALL,
            )
        }

        PotiDivider(styleType = PotiDividerStyle.SMALL)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.history_total_deposit_amount),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
            )

            Text(
                text = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    totalAmount.toMoneyString(),
                ),
                style = PotiTheme.typography.body16sb,
                color = PotiTheme.colors.black,
            )
        }
    }
}
