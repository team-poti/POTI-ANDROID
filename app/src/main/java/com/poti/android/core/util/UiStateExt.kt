package com.poti.android.core.util

import com.poti.android.core.base.UiState

inline fun <T> UiState<T>.onSuccess(block: (T) -> Unit) {
    if (this is UiState.Success) {
        block(data)
    }
}

fun <T> UiState<T>.getSuccessDataOrNull(): T? {
    return (this as? UiState.Success)?.data
}
