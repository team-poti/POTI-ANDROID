package com.poti.android.presentation.onboarding.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface ErrorText {
    data class DynamicString(val value: String) : ErrorText

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : ErrorText

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(resId, *args)
    }
}
