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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * Short 타입 텍스트 필드입니다.
 * 최대 한 줄로 노출되며, 길게 작성된 경우 포커스 아웃 시 말줄임 처리 됩니다.
 *
 * @param value 필드 입력값입니다.
 * @param onValueChanged 필드에 입력된 값을 전달합니다.
 * @param placeholder 입력값이 없을 때 표시됩니다.
 * @param modifier
 * @param keyboardType 키보드 입력 타입으로, 기본값은 Text입니다.
 * @param enabled 입력 및 포커스, 터치 이벤트 차단하고 싶다면 false로 설정합니다. 기본값 true입니다.
 * @param label 필드 상단에 표시됩니다.
 * @param error 에러가 emptyString이 아닌 경우에만 필드 하단에 표시되며, borderColor가 red로 변경됩니다.
 * @param
 * @param imeAction 키보드 액션 타입으로, 기본값은 Done 입니다. Next 설정 시 아래 위치한 필드로 포커스 이동시킬 수 있습니다.
 * @param focusRequester 필드 포커스를 외부에서 제어하고 싶을 때 사용합니다.
 */
@Composable
fun PotiShortTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    label: String = "",
    error: String = "",
    trailingIcon: @Composable (() -> Unit) = {},
    imeAction: ImeAction = ImeAction.Done,
    focusRequester: FocusRequester? = null,
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
                .heightIn(52.dp),
            keyboardType = keyboardType,
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
            singleLine = true,
            trailingIcon = trailingIcon,
        )

        FieldErrorMessage(error)
    }
}

@Preview
@Composable
private fun PotiShortTextFieldWithErrorPreview() {
    var text by remember { mutableStateOf("") }

    PotiTheme {
        PotiShortTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "플레이스홀더",
            error = text,
        )
    }
}

@Preview
@Composable
private fun PotiShortTextFieldWithLabelPreview() {
    var text by remember { mutableStateOf("") }

    PotiTheme {
        PotiShortTextField(
            value = text,
            onValueChanged = { text = it },
            placeholder = "플레이스홀더",
            label = "라벨",
        )
    }
}
