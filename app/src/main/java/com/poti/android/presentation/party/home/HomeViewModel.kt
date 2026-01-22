package com.poti.android.presentation.party.home

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.presentation.party.home.model.HomeUiEffect
import com.poti.android.presentation.party.home.model.HomeUiIntent
import com.poti.android.presentation.party.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(
        initialState = HomeUiState(),
    ) {
    override fun processIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.OnFloatingClick -> sendEffect(HomeUiEffect.NavigateToPartyCreate)
            is HomeUiIntent.OnMyArtistCategoryClick -> sendEffect(HomeUiEffect.NavigateToMyArtistCategory(intent.artistId))
            is HomeUiIntent.OnProductCardClick -> sendEffect(HomeUiEffect.NavigateToGoodsPartyList(intent.artistId))
            HomeUiIntent.LoadHomeContent -> loadHomeContent()
        }
    }

    init {
        processIntent(HomeUiIntent.LoadHomeContent)
    }

    private fun loadHomeContent() = launchScope {
        homeRepository.getHomeContent()
            .onSuccess { homeContent ->
                updateState {
                    copy(homeContentLoadState = ApiState.Success(homeContent))
                }
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        homeContentLoadState = ApiState.Failure(throwable.message ?: "Failed"),
                    )
                }
            }
    }
}
