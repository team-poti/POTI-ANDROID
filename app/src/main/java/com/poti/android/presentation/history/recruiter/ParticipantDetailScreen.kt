package com.poti.android.presentation.history.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.history.ArtistInfo
import com.poti.android.domain.model.history.DepositItem
import com.poti.android.domain.model.history.ParticipantDepositInfo
import com.poti.android.domain.model.history.ParticipantDetail
import com.poti.android.domain.model.history.ParticipantShippingInfo
import com.poti.android.domain.model.history.ProgressInfo
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.DummyParticipantManageDetail
import com.poti.android.presentation.history.component.PartyInfoSection
import com.poti.android.presentation.history.component.ProgressStatusSection
import com.poti.android.presentation.history.mapper.toUiState

@Composable
fun ParticipantDetailRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit
) {
    ParticipantDetailScreen(
        modifier = modifier,
        detail = DummyParticipantManageDetail.dummyParticipantDetail,
        onBackClick = onBackClick,
        onDetailClick = onDetailClick
    )
}

@Composable
private fun ParticipantDetailScreen(
    modifier: Modifier = Modifier,
    detail: ParticipantDetail,
    onBackClick: () -> Unit,
    onDetailClick: (Long) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(id = R.string.history_participant_detail_title)
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(PotiTheme.colors.white),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                PartyInfoSection(
                    partyId = detail.partyId,
                    artistInfo = detail.artistInfo,
                    onDetailClick = onDetailClick,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            item {
                ProgressStatusSection(
                    progressInfo = detail.progressInfo,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { PotiDivider(styleType = PotiDividerStyle.LARGE) }

            item { DepositInfoSection(info = detail.depositInfo) }

            item { PotiDivider(styleType = PotiDividerStyle.LARGE) }

            item { ShippingInfoSection(info = detail.shippingInfo) }
        }
    }
}

@Composable
private fun DepositInfoSection(info: ParticipantDepositInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.history_deposit_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black
        )
        PriceDetail(items = info.items, totalAmount = info.totalAmount)
    }
}

@Composable
private fun PriceDetail(items: List<DepositItem>, totalAmount: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            val optionType = item.toUiState()
            PotiListOptionPrice(
                itemOptionType = optionType,
                itemOptionText = item.name,
                priceText = stringResource(
                    R.string.history_participant_detail_won_unit_format,
                    item.price.toMoneyString()),
                sizeType = PotiListOptionPriceSize.SMALL,
            )
        }
        PotiDivider(styleType = PotiDividerStyle.SMALL)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.history_total_deposit_amount),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800
            )
            Text(
                text = stringResource(R.string.history_participant_detail_won_unit_format,
                    totalAmount.toMoneyString()),
                style = PotiTheme.typography.body16sb,
                color = PotiTheme.colors.black
            )
        }
    }
}

@Composable
private fun ShippingInfoSection(info: ParticipantShippingInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.history_shipping_info_title),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(R.string.history_shipping_info_format,
                    info.recipient, info.zipcode, info.address, info.phone),
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.black,
                lineHeight = PotiTheme.typography.body14m.fontSize * 1.5
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delivery),
                    contentDescription = "Delivery Method",
                    tint = PotiTheme.colors.gray800
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = info.deliveryMethod,
                    style = PotiTheme.typography.body14m,
                    color = PotiTheme.colors.gray800
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ParticipantDetailScreenPreview() {
    PotiTheme {
        ParticipantDetailRoute(onBackClick = {}, onDetailClick = {})
    }
}
