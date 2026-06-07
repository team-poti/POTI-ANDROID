package com.poti.android.presentation.party.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenHeightDp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.button.ChipButtonType
import com.poti.android.core.designsystem.component.button.PotiChipButton
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.Member

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MemberSelectBottomSheet(
    title: String,
    mainBtnText: String,
    subBtnText: String,
    onDismiss: () -> Unit,
    onMainBtnClick: () -> Unit,
    onSubBtnClick: () -> Unit,
    allMembers: List<T>,
    selectedMembers: List<T>,
    onMemberClick: (T) -> Unit,
    memberToName: (T) -> String,
    memberToId: (T) -> Long,
    mainEnabled: Boolean,
    subEnabled: Boolean,
    autoCloseSubBtn: Boolean = true,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val selectedMemberIds = remember(selectedMembers) {
        selectedMembers.map { memberToId(it) }.toSet()
    }

    PotiBottomSheet(
        onDismissRequest = onDismiss,
        text = mainBtnText,
        onClick = onMainBtnClick,
        subText = subBtnText,
        onSubClick = onSubBtnClick,
        subEnabled = subEnabled,
        enabled = mainEnabled,
        sheetState = sheetState,
        autoCloseSub = autoCloseSubBtn,
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(top = 12.dp, bottom = 16.dp, start = screenWidthDp(16.dp)),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.title18sb,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(screenHeightDp(492.dp))
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = allMembers,
                key = { memberToId(it) },
            ) { member ->
                val isSelected = memberToId(member) in selectedMemberIds

                PotiChipButton(
                    text = memberToName(member),
                    onClick = { onMemberClick(member) },
                    modifier = Modifier.fillMaxWidth(),
                    type = if (isSelected) ChipButtonType.SELECTED else ChipButtonType.DEFAULT,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MemberSelectBottomSheetPreview() {
    val members = (0L..20L).map { id ->
        Member(id, "원영")
    }

    MemberSelectBottomSheet(
        title = stringResource(R.string.action_button_continue),
        onDismiss = {},
        mainBtnText = stringResource(R.string.action_button_continue),
        onMainBtnClick = {},
        mainEnabled = true,
        subBtnText = stringResource(R.string.action_button_continue),
        onSubBtnClick = {},
        subEnabled = true,
        allMembers = members,
        selectedMembers = members,
        onMemberClick = {},
        memberToName = { it.name },
        memberToId = { it.memberId },
    )
}
