package com.poti.android.core.common.state

sealed interface ApiState<out T> {
    data object Empty : ApiState<Nothing>

    data object Loading : ApiState<Nothing>

    data class Success<out T>(
        val data: T,
    ) : ApiState<T>

    data class Failure(
        val message: String,
    ) : ApiState<Nothing>

    data object Init : ApiState<Nothing>
}
