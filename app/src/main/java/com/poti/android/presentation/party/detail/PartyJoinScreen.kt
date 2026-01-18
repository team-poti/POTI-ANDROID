package com.poti.android.presentation.party.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.display.PotiListOptionPrice
import com.poti.android.core.designsystem.component.display.PotiListOptionPriceSize
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.component.CardOptionPrice
import com.poti.android.presentation.party.detail.component.ParticipantGuidelines
import com.poti.android.presentation.party.detail.component.TotalPrice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun PartyJoinRoute(
    modifier: Modifier = Modifier,
) {
}

@Composable
private fun PartyJoinScreen(
    memberOptions: ImmutableList<FieldMenuItem>,
    deliveryOptions: ImmutableList<FieldMenuItem>,
    selectedMemberIds: Set<String>,
    selectedDeliveryId: Set<String>,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedMembers = memberOptions.filter { it.id in selectedMemberIds }
    val selectedDelivery = deliveryOptions.find { it.id in selectedDeliveryId }

    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.party_detail_title), // TODO: [지현] 닉네임 연결
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = stringResource(R.string.party_join_button),
                onClick = onJoinClick,
                enabled = true, // TODO: [지현] 활성화 연결
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 24.dp)
                    .padding(horizontal = screenWidthDp(16.dp)),
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    selectedMembers.forEach { member ->
                        PotiListOptionPrice(
                            itemOptionType = PotiItemOptionType.MEMBER,
                            itemOptionText = member.option,
                            priceText = member.price ?: "",
                            sizeType = PotiListOptionPriceSize.SMALL,
                        )
                    }

                    selectedDelivery?.let { delivery ->
                        CardOptionPrice(
                            optionType = PotiItemOptionType.DELIVERY,
                            text = delivery.option,
                            price = delivery.price ?: "",
                        )
                    }
                }

                PotiDivider(
                    styleType = PotiDividerStyle.SMALL,
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                TotalPrice(totalPrice = "12,800") // TODO: [지현] 가격 연결
            }

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            Column(
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp), vertical = 24.dp),
            ) {
                Text(
                    text = stringResource(R.string.party_join_order_info),
                    style = PotiTheme.typography.title18sb,
                    color = PotiTheme.colors.black,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.party_join_order_name_placeholder),
                        label = stringResource(R.string.filed_label_name),
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.party_join_order_name_placeholder),
                        label = stringResource(R.string.party_join_order_postal_label),
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.party_join_order_name_placeholder),
                        label = stringResource(R.string.party_join_order_postal_placeholder),
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.party_join_order_address_label),
                        label = stringResource(R.string.party_join_order_address_placeholder),
                    )

                    PotiShortTextField(
                        value = "",
                        onValueChanged = {},
                        placeholder = stringResource(R.string.party_join_order_contact_label),
                        label = stringResource(R.string.party_join_order_contact_placeholder),
                    )
                }
            }

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            ParticipantGuidelines()
        }
    }
}

@Preview
@Composable
private fun PartyJoinScreenPreview() {
    val selectedMemberIds = remember { mutableStateSetOf<String>() }
    val selectedDeliveryId = remember { mutableStateSetOf<String>() }

    PotiTheme {
        PartyJoinScreen(
            memberOptions = dummyMemberOptions.toImmutableList(),
            deliveryOptions = dummyDeliveryOptions.toImmutableList(),
            selectedMemberIds = selectedMemberIds,
            selectedDeliveryId = selectedDeliveryId,
            onBackClick = {},
            onJoinClick = {},
        )
    }
}
