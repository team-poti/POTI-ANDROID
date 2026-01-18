package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.core.designsystem.component.field.PotiDropdownField
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.dummyDeliveryOptions
import com.poti.android.presentation.party.detail.dummyMemberOptions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyJoinBottomSheet(
    memberOptions: ImmutableList<FieldMenuItem>,
    deliveryOptions: ImmutableList<FieldMenuItem>,
    selectedMemberIds: Set<String>,
    selectedDeliveryId: Set<String>,
    onMemberSelect: (FieldMenuItem) -> Unit,
    onMemberRemove: (String) -> Unit,
    onDeliverySelect: (FieldMenuItem) -> Unit,
    hasSelectedOptions: Boolean,
    totalPrice: String,
    onDismissRequest: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
) {
    val selectedMembers = memberOptions.filter { it.id in selectedMemberIds }
    val selectedDelivery = deliveryOptions.find { it.id in selectedDeliveryId }

    PotiBottomSheet( // TODO: [지현] 버튼 수정
        onDismissRequest = onDismissRequest,
        text = stringResource(R.string.action_button_continue),
        onClick = onNextClick,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
        ) {
            PotiDropdownField(
                value = "",
                placeholder = stringResource(R.string.party_join_option_member_placeholder),
                onItemClick = onMemberSelect,
                menuItems = memberOptions,
                selectedIds = selectedMemberIds,
                modifier = Modifier.padding(bottom = 28.dp),
                label = stringResource(R.string.party_join_option_member_label),
                initialOpenState = true,
            )

            PotiDropdownField(
                value = "",
                placeholder = stringResource(R.string.party_join_option_delivery_placeholder),
                onItemClick = onDeliverySelect,
                menuItems = deliveryOptions,
                selectedIds = selectedDeliveryId,
                modifier = Modifier.padding(bottom = 49.dp),
                label = stringResource(R.string.party_join_option_delivery_label),
                initialOpenState = false,
            )

            Column(
                modifier = Modifier.height(screenHeightDp(255.dp)),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                ) {
                    items(selectedMembers) { member ->
                        CardOptionPrice(
                            optionType = PotiItemOptionType.MEMBER,
                            text = member.option,
                            price = stringResource(R.string.party_option_price_won, member.price ?: "0"),
                            onDeleteClick = { onMemberRemove(member.id) },
                        )
                    }

                    selectedDelivery?.let { delivery ->
                        item {
                            CardOptionPrice(
                                optionType = PotiItemOptionType.DELIVERY,
                                text = delivery.option,
                                price = delivery.price ?: "",
                            )
                        }
                    }
                }

                if (hasSelectedOptions) {
                    PotiDivider(
                        styleType = PotiDividerStyle.SMALL,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )

                    TotalPrice(totalPrice = totalPrice)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PartyJoinBottomSheetPreview() {
    val selectedMemberIds = remember { mutableStateSetOf<String>() }
    val selectedDeliveryId = remember { mutableStateSetOf<String>() }

    PotiTheme {
        PartyJoinBottomSheet(
            memberOptions = dummyMemberOptions.toImmutableList(),
            deliveryOptions = dummyDeliveryOptions.toImmutableList(),
            selectedMemberIds = selectedMemberIds,
            selectedDeliveryId = selectedDeliveryId,
            onMemberSelect = {
                if (it.id in selectedMemberIds) {
                    selectedMemberIds.remove(it.id)
                } else {
                    selectedMemberIds.add(it.id)
                }
            },
            onMemberRemove = {
                selectedMemberIds.remove(it)
            },
            onDeliverySelect = {
                if (selectedDeliveryId.isNotEmpty()) {
                    selectedDeliveryId.clear()
                }
                selectedDeliveryId.add(it.id)
            },
            hasSelectedOptions = false,
            totalPrice = "11,000",
            onDismissRequest = {},
            onNextClick = {},
        )
    }
}
