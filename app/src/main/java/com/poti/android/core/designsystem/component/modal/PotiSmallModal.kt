package com.poti.android.core.designsystem.component.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.button.ModalButtonType
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiModalButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 스몰 모달 컴포넌트입니다. 기본 모달 컴포넌트를 래핑해, 디자인 요구사항에 따른 기본 레이아웃을 제공합니다.
 *
 * @param onDismissRequest 모달을 닫는 콜백입니다.
 * @param title "큰 내용"에 해당하는 텍스트입니다.
 * @param text "작은 내용"에 해당하는 텍스트입니다.
 * @param dismissBtnText 좌측 버튼 텍스트입니다.
 * @param confirmBtnText 우측 버튼 텍스트입니다.
 * @param onDismissBtnClick 좌측 버튼 클릭 콜백입니다.
 * @param onConfirmBtnClick 우측 버튼 클릭 콜백입니다.
 * @param modifier
 * @param dismissOnBackPress 시스템 뒤로가기 시 모달을 닫는 여부입니다. 기본값 true입니다.
 * @param dismissOnClickOutside 모달 바깥 영역 터치 시 모달을 닫는 여부입니다. 기본값 true입니다.
 */
@Composable
fun PotiSmallModal(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    dismissBtnText: String,
    confirmBtnText: String,
    onDismissBtnClick: () -> Unit,
    onConfirmBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
) {
    PotiModal(
        onDismissRequest = onDismissRequest,
        modifier = modifier.padding(horizontal = 32.dp),
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    all = 16.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.body16sb,
                textAlign = TextAlign.Center,
            )

            Text(
                text = text,
                color = PotiTheme.colors.gray800,
                style = PotiTheme.typography.body16m,
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PotiModalButton(
                    text = dismissBtnText,
                    onClick = onDismissBtnClick,
                    modifier = Modifier
                        .weight(1f),
                    type = ModalButtonType.SUB_1,
                )

                PotiModalButton(
                    text = confirmBtnText,
                    onClick = onConfirmBtnClick,
                    modifier = Modifier
                        .weight(1f),
                    type = ModalButtonType.MAIN,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PotiSmallModalPreview() {
    var showModal by remember { mutableStateOf(false) }

    PotiTheme {
        if (showModal) {
            PotiSmallModal(
                onDismissRequest = { showModal = false },
                title = "큰 내용",
                text = "작은 내용",
                dismissBtnText = "버튼1",
                confirmBtnText = "버튼2",
                onDismissBtnClick = {},
                onConfirmBtnClick = { },
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PotiFloatingButton(
                onClick = { showModal = true },
            )
        }
    }
}
