package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()

    val textStyle = PotiTheme.typography.body16sb
    val transformation = remember { PriceVisualTransformation() }

    val transformedText = remember(value) {
        transformation.filter(AnnotatedString(value)).text.text
    }

    val textWidth = remember(transformedText, textStyle) {
        density.run {
            measurer.measure(transformedText, textStyle).size.width.toDp()
        }
    }

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
            textWidth = textWidth,
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
    textWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = PotiTheme.colors

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            }
            .drawWithCache {
                val minPx = 42.dp.toPx()
                val textPx = textWidth.toPx()
                val underlinePx = max(minPx, textPx)

                val strokePx = 2.dp.toPx()
                val yOffset = strokePx / 2 - 4.dp.toPx()

                onDrawBehind {
                    val y = size.height - yOffset

                    drawLine(
                        color = colors.gray300,
                        start = Offset(size.width - underlinePx, y),
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
        singleLine = true,
        visualTransformation = transformation,
        decorationBox = { innerTextField ->
            innerTextField()
        },
        enabled = enabled,
    )
}

private class PriceVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val text = text.text

        val textWithComma = when (text.length) {
            0 -> text
            else -> text.toInt().toMoneyString()
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == text.length) {
                    return textWithComma.length
                }

                val numbersAtferCursor = text.length - offset

                val commasAfterCursor = if (numbersAtferCursor % 3 == 0) {
                    numbersAtferCursor / 3 - 1
                } else {
                    numbersAtferCursor / 3
                }

                return textWithComma.length - numbersAtferCursor - commasAfterCursor
            }

            override fun transformedToOriginal(offset: Int): Int {
                var commasBeforeCursor = 0

                textWithComma.forEachIndexed { index, char ->
                    if (index >= offset) return@forEachIndexed

                    if (char == ',') {
                        commasBeforeCursor += 1
                    }
                }

                return offset - commasBeforeCursor
            }
        }

        return TransformedText(
            text = AnnotatedString(textWithComma),
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
