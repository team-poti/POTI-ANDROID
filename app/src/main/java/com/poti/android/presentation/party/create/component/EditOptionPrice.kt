package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.display.PotiCheckBox
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun EditOptionPrice(
    option: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean? = null,
    onCheckboxClick: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done,
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
            measurer.measure(transformedText, textStyle).size.width.toDp() + 2.dp
        }
    }

    var wonWidth by remember { mutableStateOf(0.dp) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row {
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
                onValueChanged = onValueChanged,
                imeAction = imeAction,
                transformation = transformation,
                textStyle = textStyle,
                modifier = Modifier.width(textWidth),
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = "원",
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        wonWidth = with(density) { coordinates.size.width.toDp() }
                    },
                color = PotiTheme.colors.black,
                style = PotiTheme.typography.body16m,
            )
        }

        PotiDivider(
            styleType = PotiDividerStyle.SMALL,
            modifier = Modifier
                .padding(end = wonWidth + 4.dp)
                .widthIn(min = 42.dp)
                .width(textWidth),
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
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        textStyle = textStyle.copy(
            color = PotiTheme.colors.black,
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
    )
}

private class PriceVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val text = text.text

        val textWithComma = buildString {
            for (i in text.indices) {
                append(text[i])
                if (i != text.length - 1 && (text.length - 1 - i) % 3 == 0) {
                    append(',')
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == text.length) {
                    return textWithComma.length
                }

                val numbersAferCursor = text.length - offset

                val commasAferCursor = if (numbersAferCursor % 3 == 0) {
                    numbersAferCursor / 3 - 1
                } else {
                    numbersAferCursor / 3
                }

                return textWithComma.length - numbersAferCursor - commasAferCursor
            }

            override fun transformedToOriginal(offset: Int): Int {
                val commasBeforeCursor = textWithComma.take(offset).count { it == ',' }

                return offset - commasBeforeCursor
            }
        }

        return TransformedText(
            text = AnnotatedString(textWithComma),
            offsetMapping = offsetMapping,
        )
    }
}

@Preview
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
            )

            EditOptionPrice(
                option = "옵션",
                value = text2,
                onValueChanged = { text2 = it },
                imeAction = ImeAction.Next,
                isChecked = false,
                onCheckboxClick = {},
            )

            EditOptionPrice(
                option = "옵션".repeat(50),
                value = text3,
                onValueChanged = { text3 = it },
                imeAction = ImeAction.Done,
            )
        }
    }
}
