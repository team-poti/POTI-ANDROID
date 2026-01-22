package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.display.PotiErrorMessage
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * Long 타입 텍스트 필드입니다. 최소 높이 160이며, 컨텍츠에 따라 높이가 늘어납니다.
 *
 * @param value 필드 입력값입니다.
 * @param onValueChanged 필드에 입력된 값을 전달합니다.
 * @param placeholder 입력값이 없을 때 표시됩니다.
 * @param modifier
 * @param label 필드 상단에 표시됩니다.
 * @param error 에러가 emptyString이 아닌 경우에만 필드 하단에 표시되며, borderColor가 red로 변경됩니다.
 * @param imeAction 키보드 액션 타입으로, 기본값은 Default 입니다. Done 설정 시 키보드 닫힘, Next 설정 시 아래 위치한 필드로 포커스 이동시킬 수 있습니다.
 * @param focusRequester 필드 포커스를 외부에서 제어하고 싶을 때 사용합니다.
 * @param enabled 입력 및 포커스, 터치 이벤트 차단하고 싶다면 false로 설정합니다. 기본값 true입니다.
 */
@Composable
fun PotiLongTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String = "",
    error: String = "",
    imeAction: ImeAction = ImeAction.Default,
    focusRequester: FocusRequester = remember { FocusRequester() },
    enabled: Boolean = true,
) {
    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val status = when {
        error.isNotEmpty() -> FieldStatus.ERROR
        isFocused -> FieldStatus.FOCUS
        else -> FieldStatus.DEFAULT
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FieldLabel(label)

        PotiBasicField(
            value = value,
            onValueChanged = onValueChanged,
            placeholder = placeholder,
            borderColor = status.borderColor,
            backgroundColor = PotiTheme.colors.white,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(160.dp),
            enabled = enabled,
            imeAction = imeAction,
            onDoneAction = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            onNextAction = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onFocusChanged = { isFocused = it },
            focusRequester = focusRequester,
            singleLine = false,
        )

        if (error.isNotBlank()) {
            PotiErrorMessage(message = error)
        }
    }
}

@Preview
@Composable
private fun PotiLongTextFieldWithErrorPreview() {
    var text by remember { mutableStateOf("") }

    PotiTheme {
        PotiLongTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "플레이스홀더",
            error = "에러 메시지",
        )
    }
}

@Preview
@Composable
private fun PotiLongTextFieldWithLabelPreview() {
    var text by remember { mutableStateOf("") }

    PotiTheme {
        PotiLongTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "플레이스홀더\n플레이스",
            label = "라벨",
        )
    }
}
