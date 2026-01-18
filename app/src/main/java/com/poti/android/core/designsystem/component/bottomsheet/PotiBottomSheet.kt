package com.poti.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPageType
import com.poti.android.core.designsystem.theme.PotiTheme
import kotlinx.coroutines.launch

/**
 * 바텀시트 기본 레이아웃입니다.
 *
 * @param onDismissRequest 바텀시트를 닫는 콜백입니다.
 * @param text Main 버튼 텍스트입니다.
 * @param onClick Main 버튼 콜백입니다.
 * @param modifier
 * @param sheetState 바텀시트 상태로 expanded 상태 등을 조정하고 싶을 때 사용합니다.
 * @param subText Sub 버튼 텍스트입니다. lg 스타일일 때 사용합니다.
 * @param onSubClick Sub 버튼 콜백입니다. lg 스타일일 때 사용합니다.
 * @param shouldDismissOnBackPress 시스템 뒤로가기 시 바텀시트가 닫히는지 여부입니다. 기본값 true로, 닫히도록 설정되어 있습니다.
 * @param content
 *
 * @author 도연
 * @sample PotiBottomSheetPreview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotiBottomSheet(
    onDismissRequest: () -> Unit,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isButtonEnabled: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(),
    subText: String? = null,
    onSubClick: (() -> Unit)? = null,
    shouldDismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()

    val properties = remember(shouldDismissOnBackPress) {
        ModalBottomSheetProperties(
            shouldDismissOnBackPress = shouldDismissOnBackPress,
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = PotiTheme.colors.white,
        scrimColor = PotiTheme.colors.blackA40,
        dragHandle = null,
        properties = properties,
    ) {
        PotiHeaderPage(
            onNavigationClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismissRequest()
                    }
                }
            },
            potiHeaderPageType = PotiHeaderPageType.CLOSE,
            modifier = Modifier.padding(top = 4.dp),
        )

        content()

        BottomSheetButton(
            text = text,
            onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onClick()
                        onDismissRequest()
                    }
                }
            },
            enabled = isButtonEnabled,
            subText = subText,
            onSubClick = {
                onSubClick?.let {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onSubClick()
                            onDismissRequest()
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun BottomSheetButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    subText: String? = null,
    onSubClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(PotiTheme.colors.white)
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (subText != null && onSubClick != null) {
            PotiActionButton(
                text = subText,
                onClick = onSubClick,
                type = ActionButtonType.SECONDARY_SUB,
                modifier = Modifier.weight(119f),
            )
        }

        PotiActionButton(
            text = text,
            onClick = onClick,
            type = ActionButtonType.SECONDARY_MAIN,
            modifier = Modifier.weight(216f),
            enabled = enabled,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PotiBottomSheetPreview() {
    var showSmallBottomSheet by remember { mutableStateOf(false) }
    var showLargeBottomSheet by remember { mutableStateOf(false) }

    PotiTheme {
        if (showSmallBottomSheet) {
            PotiBottomSheet(
                text = "버튼",
                onClick = {},
                onDismissRequest = { showSmallBottomSheet = false },
                content = {
                    Text(
                        text = "멤버",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        items(5) {
                            Text(
                                text = "멤버",
                                modifier = Modifier
                                    .padding(vertical = 8.dp),
                            )
                        }
                    }
                },
            )
        }

        if (showLargeBottomSheet) {
            PotiBottomSheet(
                text = "버튼",
                onClick = {},
                onDismissRequest = { showLargeBottomSheet = false },
                subText = "버튼",
                onSubClick = {},
                content = {
                    Text(
                        text = "멤버",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        items(5) {
                            Text(
                                text = "멤버",
                                modifier = Modifier
                                    .padding(vertical = 8.dp),
                            )
                        }
                    }
                },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            PotiFloatingButton(
                onClick = { showSmallBottomSheet = true },
            )

            PotiFloatingButton(
                onClick = { showLargeBottomSheet = true },
            )
        }
    }
}
