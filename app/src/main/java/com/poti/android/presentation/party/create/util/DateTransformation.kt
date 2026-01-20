package com.poti.android.presentation.party.create.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val text = text.text

        val textWithDash = buildString {
            text.forEachIndexed { index, char ->
                append(char)
                if (index == 3 || index == 5) append("-")
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val numbersBeforeCursor = offset

                return when {
                    numbersBeforeCursor < 4 -> numbersBeforeCursor
                    numbersBeforeCursor < 6 -> numbersBeforeCursor + 1
                    else -> numbersBeforeCursor + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val dashesBeforeCursor = when {
                    offset <= 4 -> 0
                    offset <= 7 -> 1
                    else -> 2
                }

                return offset - dashesBeforeCursor
            }
        }

        return TransformedText(
            text = AnnotatedString(textWithDash),
            offsetMapping = offsetMapping,
        )
    }
}
