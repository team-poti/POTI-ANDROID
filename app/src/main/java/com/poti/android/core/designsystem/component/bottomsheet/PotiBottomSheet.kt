package com.poti.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 바텀시트 기본 레이아웃입니다.
 *
 * @param onDismissRequest 바텀시트를 닫는 콜백입니다.
 * @param modifier
 * @param skipPartiallyExpanded 바텀시트 content가 길 때, 바텀시트가 부분적으로 열리고, 사용자가 드래그해야 전체 content가 노출되는지 여부를 조정합니다. 기본값 true여서, 기본값 사용 시 바텀시트 열면 모든 content가 한 번에 보이게 됩니다.
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
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = true,
    shouldDismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    )

    val properties = remember {
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
        // TODO: [도연] Navigation 컴포넌트 병합 시, header-pager로 대체
        BottomSheetHeader(
            onXIconClick = onDismissRequest,
            modifier = Modifier.padding(top = 4.dp),
        )

        content()
    }
}

// TODO: [도연] Navigation 컴포넌트 병합 시, 삭제
@Composable
private fun BottomSheetHeader(
    onXIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_x),
        tint = PotiTheme.colors.black,
        contentDescription = null,
        modifier = modifier
            .noRippleClickable(onClick = onXIconClick)
            .padding(all = 12.dp),
    )
}

@Preview
@Composable
private fun PotiBottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }

    PotiTheme {
        if (showBottomSheet) {
            PotiBottomSheet(
                onDismissRequest = { showBottomSheet = false },
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

                    PotiActionButton(
                        text = "계속",
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 4.dp, bottom = 14.dp),
                    )
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
