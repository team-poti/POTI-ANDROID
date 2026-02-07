package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.poti.android.R
import com.poti.android.core.common.extension.toMoneyString
import com.poti.android.core.designsystem.component.display.PotiCheckBox
import com.poti.android.core.designsystem.theme.PotiTheme
import kotlin.math.max

private const val MAX_LENGTH = 9

@Composable
fun EditOptionPrice(
    option: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean? = null,
    onCheckboxClick: (() -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit) = {},
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
) {
    val textStyle = PotiTheme.typography.body16sb
    val transformation = remember { PriceVisualTransformation() }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        isChecked?.let {
            PotiCheckBox(
                selected = isChecked,
                onClick = onCheckboxClick,
            )

            Spacer(Modifier.width(8.dp))
        }

        Text(
            text = option,
            modifier = Modifier
                .weight(1f),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16m,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        Spacer(Modifier.width(12.dp))

        OptionTextField(
            value = value,
            onValueChanged = { newValue ->
                if (!newValue.isDigitsOnly()) return@OptionTextField
                if (newValue.length > MAX_LENGTH) return@OptionTextField

                val adjusted = newValue.toIntOrNull()?.toString() ?: ""

                onValueChanged(adjusted)
            },
            imeAction = imeAction,
            transformation = transformation,
            textStyle = textStyle,
            onFocusChanged = onFocusChanged,
            enabled = enabled,
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = stringResource(R.string.create_label_won),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16m,
        )
    }
}

@Composable
private fun OptionTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    imeAction: ImeAction,
    transformation: VisualTransformation,
    textStyle: TextStyle,
    onFocusChanged: (Boolean) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = PotiTheme.colors

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .widthIn(42.dp)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            }
            .drawWithCache {
                val minPx = 42.dp.toPx()
                val strokePx = 2.dp.toPx()
                val yOffset = strokePx / 2 - 4.dp.toPx()

                onDrawBehind {
                    val underlineWidth = max(size.width, minPx)
                    val y = size.height - yOffset

                    drawLine(
                        color = colors.gray300,
                        start = Offset(size.width - underlineWidth, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round,
                    )
                }
            },
        textStyle = textStyle.copy(
            color = PotiTheme.colors.black,
            textAlign = TextAlign.End,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Down,
                )
            },
        ),
        maxLines = 1,
        visualTransformation = transformation,
        decorationBox = { innerTextField ->
            innerTextField()
        },
        enabled = enabled,
    )
}

private class PriceVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val transformedText = originalText.toIntOrNull()?.toMoneyString() ?: originalText

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset >= originalText.length) return transformedText.length

                val numbersAfterCursor = originalText.length - offset

                val commasAfterCursor = if (numbersAfterCursor % 3 == 0) {
                    numbersAfterCursor / 3 - 1
                } else {
                    numbersAfterCursor / 3
                }

                return transformedText.length - numbersAfterCursor - commasAfterCursor
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset >= transformedText.length) return originalText.length

                // 목표 = 커서 좌측 원본 길이
                //     = 원본 전체 길이 - 원본 우측 길이
                //     = 원본 전체 길이 - (변경 우측 길이 - 변형 우측 콤마 개수)
                val rightOffset = transformedText.length - offset
                val rightCommas = rightOffset / 4

                return originalText.length - (rightOffset - rightCommas)
            }
        }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = offsetMapping,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionTextFieldPreview() {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf("") }

    PotiTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            EditOptionPrice(
                option = "옵션",
                value = text1,
                onValueChanged = { text1 = it },
                imeAction = ImeAction.Next,
                isChecked = true,
                onCheckboxClick = {},
                onFocusChanged = {},
            )

            EditOptionPrice(
                option = "옵션",
                value = text2,
                onValueChanged = { text2 = it },
                imeAction = ImeAction.Next,
                isChecked = false,
                onCheckboxClick = {},
                onFocusChanged = {},
            )

            EditOptionPrice(
                option = "옵션".repeat(50),
                value = text3,
                onValueChanged = { text3 = it },
                imeAction = ImeAction.Done,
                onFocusChanged = {},
            )
        }
    }
}
