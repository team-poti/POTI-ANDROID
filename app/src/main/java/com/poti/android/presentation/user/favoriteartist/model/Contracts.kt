package com.poti.android.presentation.user.favoriteartist.model

import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Artist
import kotlinx.collections.immutable.ImmutableList

data class FavoriteArtistUiState(
    val artists: ApiState<ImmutableList<Artist>> = ApiState.Loading,
    val selectedArtistId: Long? = null,
    val isSaving: Boolean = false,
) : UiState {
    val isSaveEnabled: Boolean
        get() = selectedArtistId != null && !isSaving
}

sealed interface FavoriteArtistUiIntent : UiIntent {
    data object OnBackClick : FavoriteArtistUiIntent

    data class OnArtistSelect(val artistId: Long) : FavoriteArtistUiIntent

    data object OnSaveClick : FavoriteArtistUiIntent

    data object OnInquiryClick : FavoriteArtistUiIntent
}

sealed interface FavoriteArtistUiEffect : UiEffect {
    data object NavigateBack : FavoriteArtistUiEffect

    data object SaveSuccess : FavoriteArtistUiEffect
}
