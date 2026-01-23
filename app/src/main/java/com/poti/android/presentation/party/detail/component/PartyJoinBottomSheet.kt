package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.component.field.FieldMenuItem
import com.poti.android.core.designsystem.component.field.PotiDropdownField
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.party.detail.model.PartyDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyJoinBottomSheet(
    uiState: PartyDetailUiState,
    onMemberSelect: (FieldMenuItem) -> Unit,
    onMemberRemove: (String) -> Unit,
    onDeliverySelect: (FieldMenuItem) -> Unit,
    onDismissRequest: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
) {
    var isMemberFieldOpen by remember { mutableStateOf(true) }
    var isDeliveryFieldOpen by remember { mutableStateOf(false) }

    val isDropdownOpened = isMemberFieldOpen || isDeliveryFieldOpen

    PotiBottomSheet(
        onDismissRequest = onDismissRequest,
        text = stringResource(R.string.action_button_continue),
        onClick = onNextClick,
        enabled = uiState.isBottomSheetButtonEnable,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .padding(horizontal = screenWidthDp(16.dp)),
        ) {
            PotiDropdownField(
                value = "",
                placeholder = stringResource(R.string.party_join_option_member_placeholder),
                onItemClick = onMemberSelect,
                menuItems = uiState.memberMenuItems,
                selectedIds = uiState.selectedMemberIds,
                modifier = Modifier.padding(bottom = 28.dp),
                label = stringResource(R.string.party_join_option_member_label),
                initialOpenState = true,
                onExpandedChange = { isOpen -> isMemberFieldOpen = isOpen },
            )

            PotiDropdownField(
                value = uiState.selectedDelivery?.option ?: "",
                placeholder = stringResource(R.string.party_join_option_delivery_placeholder),
                onItemClick = onDeliverySelect,
                menuItems = uiState.deliveryMenuItems,
                selectedIds = uiState.selectedDeliveryIds,
                modifier = Modifier.padding(bottom = 49.dp),
                label = stringResource(R.string.party_join_option_delivery_label),
                initialOpenState = false,
                onExpandedChange = { isOpen -> isDeliveryFieldOpen = isOpen },
            )

            Column(
                modifier = Modifier.height(screenHeightDp(255.dp)),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(if (isDropdownOpened) 0.8f else 1.0f),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                    ) {
                        items(
                            items = uiState.selectedMembers,
                            key = { it.id },
                        ) { member ->
                            CardOptionPrice(
                                optionType = PotiItemOptionType.MEMBER,
                                text = member.option,
                                price = stringResource(R.string.party_option_price_won, member.price ?: "0"),
                                onDeleteClick = { onMemberRemove(member.id) },
                            )
                        }

                        uiState.selectedDelivery?.let { delivery ->
                            item {
                                CardOptionPrice(
                                    optionType = PotiItemOptionType.DELIVERY,
                                    text = delivery.option,
                                    price = stringResource(R.string.party_option_price_won, delivery.price ?: "0"),
                                )
                            }
                        }
                    }

                    if (isDropdownOpened) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(PotiTheme.colors.white.copy(alpha = 0.8f)),
                        )
                    }
                }

                if (uiState.hasSelectedOptions) {
                    PotiDivider(
                        styleType = PotiDividerStyle.SMALL,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )

                    TotalPrice(totalPrice = uiState.totalPrice)
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
            uiState = PartyDetailUiState(),
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
            onDismissRequest = {},
            onNextClick = {},
        )
    }
}
