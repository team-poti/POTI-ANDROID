package com.poti.android.core.designsystem.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
internal fun PotiBasicField(
    value: String,
    onValueChaged: (String) -> Unit,
    placeholder: String,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onDoneAction: () -> Unit = {},
    onNextAction: () -> Unit = {},
    onSearchAction: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    singleLine: Boolean = true,
    trailingIcon: @Composable () -> Unit = {},
    enabled: Boolean = true,
) {
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChaged,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged(focusState.isFocused)
            },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDoneAction()
                // 사용자가 별도 동작을 정의했더라도, 기본적으로 키보드는 닫히게 함
                keyboardController?.hide()
            },
            onNext = {
                onNextAction()
                // 사용자가 별도 동작을 정의했더라도, 기본적으로 포커스는 넘어가게 함
                focusManager.moveFocus(FocusDirection.Next)
            },
            onSearch = {
                onSearchAction()
                // 검색은 보통 키보드를 닫거나 유지하거나 기획에 따라 다름 (보통 닫음)
                keyboardController?.hide()
            },
        ),
        enabled = enabled,
        textStyle = PotiTheme.typography.body16m.copy(
            color = PotiTheme.colors.black,
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()

                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = PotiTheme.colors.gray700,
                            style = PotiTheme.typography.body16m,
                        )
                    }

                    if (singleLine && !isFocused && value.isNotEmpty()) {
                        Text(
                            text = value,
                            modifier = Modifier
                                .background(backgroundColor),
                            color = PotiTheme.colors.black,
                            style = PotiTheme.typography.body16m,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                trailingIcon()
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PotiBasicFieldPreview() {
    var text by remember { mutableStateOf("") }

    PotiBasicField(
        value = text,
        onValueChaged = { text = it },
        placeholder = "placeholrder",
        borderColor = PotiTheme.colors.black,
        backgroundColor = PotiTheme.colors.gray300,
        modifier = Modifier.padding(50.dp),
    )
}
