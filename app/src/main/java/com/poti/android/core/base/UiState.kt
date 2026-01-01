package com.poti.android.core.base

sealed interface UiState<out T> {
    data object Empty : UiState<Nothing>

    data object Loading : UiState<Nothing>

    data class Success<out T>(
        val data: T
    ) : UiState<T>

    data class Failure(
        val message: String
    ) : UiState<Nothing>

    data object Init : UiState<Nothing>
}
