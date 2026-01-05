package com.poti.android.core.util

import com.poti.android.core.base.ApiState

inline fun <T> ApiState<T>.onSuccess(block: (T) -> Unit) {
    if (this is ApiState.Success) {
        block(data)
    }
}

fun <T> ApiState<T>.getSuccessDataOrNull(): T? {
    return (this as? ApiState.Success)?.data
}
