package com.poti.android.core.designsystem.component.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiModalButton
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * 기본 모달 컴포넌트입니다.
 *
 * @param onDismissRequest 모달을 닫는 콜백입니다.
 * @param modifier
 * @param dismissOnBackPress 시스템 뒤로가기 시 모달을 닫는 여부입니다. 기본값 true입니다.
 * @param dismissOnClickOutside 모달 바깥 영역 터치 시 모달을 닫는 여부입니다. 기본값 true입니다.
 * @param content
 *
 * @author 도연
 * @sample PotiModalPreview
 */
@Composable
internal fun PotiModal(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable () -> Unit,
) {
    val dialogProperties = remember(dismissOnBackPress, dismissOnClickOutside) {
        DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        )
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = dialogProperties,
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.setDimAmount(0.4f)
        }

        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            color = PotiTheme.colors.white,
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PotiModalPreview() {
    var showModal by remember { mutableStateOf(false) }

    PotiTheme {
        if (showModal) {
            PotiModal(
                modifier = Modifier.padding(10.dp),
                onDismissRequest = { showModal = false },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    Text(
                        text = "거래는 어땠나요?\n별점으로 간단히 평가해주세요",
                        modifier = Modifier
                            .padding(all = 20.dp)
                            .fillMaxWidth(),
                    )

                    PotiModalButton(
                        text = "버튼",
                        onClick = {},
                    )
                }
            }
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
