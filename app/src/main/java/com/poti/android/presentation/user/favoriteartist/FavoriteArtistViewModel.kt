package com.poti.android.presentation.user.favoriteartist

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.artist.GetArtistsUseCase
import com.poti.android.domain.usecase.user.UpdateFavoriteArtistUseCase
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiEffect
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiIntent
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiState
import com.poti.android.presentation.user.mypage.navigation.MyPageRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteArtistViewModel @Inject constructor(
    private val getArtistsUseCase: GetArtistsUseCase,
    private val updateFavoriteArtistUseCase: UpdateFavoriteArtistUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<FavoriteArtistUiState, FavoriteArtistUiIntent, FavoriteArtistUiEffect>(
        initialState = FavoriteArtistUiState(),
    ) {
    private val favoriteArtistName = savedStateHandle.toRoute<MyPageRoute.FavoriteArtist>().favoriteArtistName

    init {
        fetchArtists()
    }

    override fun processIntent(intent: FavoriteArtistUiIntent) {
        when (intent) {
            FavoriteArtistUiIntent.OnBackClick -> sendEffect(FavoriteArtistUiEffect.NavigateBack)
            is FavoriteArtistUiIntent.OnArtistSelect -> selectArtist(intent.artistId)
            FavoriteArtistUiIntent.OnSaveClick -> saveFavoriteArtist()
        }
    }

    private fun fetchArtists() = launchScope {
        getArtistsUseCase()
            .onSuccess { artists ->
                val currentArtistId = artists.firstOrNull { it.name == favoriteArtistName }?.artistId

                updateState {
                    copy(
                        artists = ApiState.Success(artists.toImmutableList()),
                        selectedArtistId = currentArtistId,
                    )
                }
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
