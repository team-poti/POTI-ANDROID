package com.poti.android.presentation.history.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiItemOptionType
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun HistoryParticipantDropdown(
    userName: String,
    userImageUrl: String,
    depositItems: List<DepositItem>,
    depositTotalPrice: Int,
    detailState: DetailState,
    stageType: ParticipantStateLabelStage,
    statusType: ParticipantStateLabelStatus,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp,
                    horizontal = screenWidthDp(16.dp)),
        ) {
            ParticipantDropdownHeader(
                name = userName,
                stageType = stageType,
                statusType = statusType,
                expanded = isExpanded,
                onToggle = { onToggle() },
            )
            AnimatedVisibility(visible = isExpanded) {
                HistoryParticipantDetail(
                    userName = userName,
                    userImageUrl = userImageUrl,
                    depositItems = depositItems,
                    detailState = detailState,
                    totalPrice = depositTotalPrice,
                    modifier = Modifier.padding(top = 20.dp),
                )
            }
        }
    }
}

@Composable
private fun ParticipantDropdownHeader(
    name: String,
    stageType: ParticipantStateLabelStage,
    statusType: ParticipantStateLabelStatus,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = PotiTheme.typography.body16m,
            color = PotiTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        )
        HistoryParticipantStateLabel(
            sizeType = ParticipantStateLabelSize.SMALL,
            stageType = stageType,
            statusType = statusType,
            modifier = Modifier.padding(vertical = 2.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))

        Crossfade(
            targetState = expanded
        ) { expand ->
            Icon(
                painter = painterResource(
                    id = if (expand) {
                        R.drawable.ic_arrow_up_lg
                    } else {
                        R.drawable.ic_arrow_down_lg
                    },
                ),
                contentDescription = null,
                tint = PotiTheme.colors.gray700,
            )
        }
    }
}

@Preview
@Composable
fun HistoryParticipantDropdownPreview() {
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
    var isExpanded by remember { mutableStateOf(false) }

    PotiTheme {
        HistoryParticipantDropdown(
            userName = "어쩌구저쩌구".repeat(20),
            userImageUrl = "",
            depositItems = depositItems,
            depositTotalPrice = depositItems.sumOf { it.price },
            detailState = DetailState.AfterDelivery(
                name = "어쩌구",
                delivery = "(01234) 서울특별시 솝트구 다솝로 456",
                contact = "010-xxxx-xxxx",
                invoice = "우체국 37249720348093",
            ),
            stageType = ParticipantStateLabelStage.DELIVERY,
            statusType = ParticipantStateLabelStatus.DONE,
            isExpanded = isExpanded,
            onToggle = { isExpanded = !isExpanded },
            modifier = Modifier
                .width(375.dp)
                .padding(20.dp),
        )
    }
}
