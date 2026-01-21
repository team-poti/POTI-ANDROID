package com.poti.android.core.designsystem.component.bottomsheet

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ChipButtonType
import com.poti.android.core.designsystem.component.button.PotiChipButton
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 필터링을 위해 멤버를 선택할 수 있는 바텀시트입니다.
 *
 * @param title 리스트 상단에 노출되는 문구입니다.
 * @param onDismiss 바텀시트를 닫는 콜백입니다.
 * @param mainBtnText 우측 메인 버튼 텍스트입니다.
 * @param onMainBtnClick 우측 메인 버튼 클릭 콜백입니다.
 * @param mainEnabled 우측 메인 버튼 활성 여부입니다.
 * @param subBtnText 좌측 서브 버튼 텍스트입니다.
 * @param onSubBtnClick 좌측 서브 버튼 클릭 콜백입니다.
 * @param subEnabled 좌측 서브 버튼 활성 여부입니다.
 * @param members 리스트에 표시될 멤버 이름 리스트입니다. 모델 의존성이 없으니 뷰모델에서 List<String>으로 매핑해서 넣어주세요.
 * @param selectedIndices 선택된 멤버 인덱스를 저장하는 셋입니다.
 * @param onMemberClick 멤버 클릭 콜백입니다. 선택된 인덱스를 전달합니다. 본 인덱스를 사용해 selectedIndices를 관리해주세요.
 *
 * @author 도연
 * @sample MemberSelectBottomSheetLongVerPreview
 */

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
    selectedIndices: Set<Int>,
    onMemberClick: (Int) -> Unit,
    autoCloseSubBtn: Boolean = true,
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
        autoCloseSub = autoCloseSubBtn,
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
                val isSelected = index in selectedIndices

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
        selectedIndices = setOf(1),
        onMemberClick = {},
    )
}

@Preview
@Composable
private fun MemberSelectBottomSheetLongVerPreview() {
    val members = listOf("원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진", "원영", "유진")
    var selectedIndices by remember { mutableStateOf(setOf<Int>()) }

    var showBottomSheet by remember { mutableStateOf(false) }

    PotiTheme {
        if (showBottomSheet) {
            MemberSelectBottomSheet(
                title = R.string.action_button_continue,
                onDismiss = { showBottomSheet = false },
                mainBtnText = R.string.action_button_continue,
                onMainBtnClick = {},
                mainEnabled = true,
                subBtnText = R.string.action_button_continue,
                onSubBtnClick = {},
                subEnabled = true,
                members = members,
                selectedIndices = selectedIndices,
                onMemberClick = { index ->
                    selectedIndices =
                        if (selectedIndices.contains(index)) {
                            selectedIndices - index
                        } else {
                            selectedIndices + index
                        }
                },
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PotiFloatingButton(
                onClick = { showBottomSheet = true },
            )
        }
    }
}
