package com.poti.android.core.designsystem.component.bottomsheet

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ChipButtonType
import com.poti.android.core.designsystem.component.button.PotiChipButton
import com.poti.android.core.designsystem.theme.PotiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberSelectBottomSheet(
    @StringRes title: Int,
    onDismiss: () -> Unit,
    @StringRes mainBtnText: Int,
    onMainBtnClick: () -> Unit,
    mainEnabled: Boolean,
    @StringRes subBtnText: Int,
    onSubBtnClick: () -> Unit,
    subEnabled: Boolean,
    members: List<String>,
    selectedIndics: Set<Int>,
    onMemberClick: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    PotiBottomSheet(
        onDismissRequest = onDismiss,
        text = stringResource(mainBtnText),
        onClick = onMainBtnClick,
        subText = stringResource(subBtnText),
        onSubClick = onSubBtnClick,
        subEnabled = subEnabled,
        enabled = mainEnabled,
        sheetState = sheetState,
    ) {
        Text(
            text = stringResource(title),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 16.dp, start = screenWidthDp(16.dp))
                .background(PotiTheme.colors.white),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.title18sb,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(492.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(members) { index, member ->
                val isSelected = index in selectedIndics

                PotiChipButton(
                    text = member,
                    onClick = { onMemberClick(index) },
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
    MemberSelectBottomSheet(
        title = R.string.action_button_continue,
        onDismiss = {},
        mainBtnText = R.string.action_button_continue,
        onMainBtnClick = {},
        mainEnabled = true,
        subBtnText = R.string.action_button_continue,
        onSubBtnClick = {},
        subEnabled = true,
        members = listOf("원영", "유진"),
        selectedIndics = setOf(1),
        onMemberClick = {},
    )
}

@Preview
@Composable
private fun MemberSelectBottomSheetLongVerPreview() {
    MemberSelectBottomSheet(
        title = R.string.action_button_continue,
        onDismiss = {},
        mainBtnText = R.string.action_button_continue,
        onMainBtnClick = {},
        mainEnabled = true,
        subBtnText = R.string.action_button_continue,
        onSubBtnClick = {},
        subEnabled = true,
        members = listOf("원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진"),
        selectedIndics = setOf(1),
        onMemberClick = {},
    )
}
