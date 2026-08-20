package com.poti.android.presentation.user.favoriteartist

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.artist.GetArtistsUseCase
import com.poti.android.domain.usecase.user.UpdateFavoriteArtistUseCase
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiEffect
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiIntent
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteArtistViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val updateFavoriteArtistUseCase: UpdateFavoriteArtistUseCase,
) : BaseViewModel<FavoriteArtistUiState, FavoriteArtistUiIntent, FavoriteArtistUiEffect>(
        initialState = FavoriteArtistUiState(),
    ) {
    init {
        fetchArtists()
    }

    override fun processIntent(intent: FavoriteArtistUiIntent) {
        when (intent) {
            FavoriteArtistUiIntent.OnBackClick -> sendEffect(FavoriteArtistUiEffect.NavigateBack)
            is FavoriteArtistUiIntent.OnArtistSelect -> selectArtist(intent.artistId)
            FavoriteArtistUiIntent.OnSaveClick -> saveFavoriteArtist()
            FavoriteArtistUiIntent.OnInquiryClick -> Unit // TODO: [천민재] 추후 구글폼 링크 연결
        }
    }

    private fun fetchArtists() = launchScope {
        getArtistsUseCase()
            .onSuccess { artists ->
                updateState { copy(artists = ApiState.Success(artists.toImmutableList())) }
            }
            .onFailure { error ->
                updateState { copy(artists = ApiState.Failure(error.message ?: "Failed")) }
            }
    }

    private fun selectArtist(artistId: Long) {
        updateState { copy(selectedArtistId = artistId) }
    }

    private fun saveFavoriteArtist() {
        val artistId = uiState.value.selectedArtistId ?: return
        if (uiState.value.isSaving) return

        launchScope {
            updateState { copy(isSaving = true) }

            updateFavoriteArtistUseCase(artistId)
                .onSuccess {
                    updateState { copy(isSaving = false) }
                    sendEffect(FavoriteArtistUiEffect.SaveSuccess)
                }
                .onFailure { error ->
                    Timber.e(error, "최애 아티스트 저장 실패")
                    updateState { copy(isSaving = false) }
                }
        }
    }
}
