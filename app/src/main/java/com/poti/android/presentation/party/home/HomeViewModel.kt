package com.poti.android.presentation.party.home

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.presentation.party.home.model.HomeUiEffect
import com.poti.android.presentation.party.home.model.HomeUiEffect.*
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
            HomeUiIntent.OnFloatingClick -> sendEffect(NavigateToPartyCreate)
            is HomeUiIntent.OnMyArtistCategoryClick -> sendEffect(NavigateToMyArtistCategory(if (uiState.value.artistIdToNull) null else intent.artistId))
            is HomeUiIntent.OnProductCardClick -> sendEffect(NavigateToGoodsPartyList(intent.artistId, intent.title))
            HomeUiIntent.LoadHomeContent -> loadHomeContent()
            HomeUiIntent.OnOtherProductCategoryClick -> sendEffect(NavigateToOtherProductCategory)
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
                validateArtistId()
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        homeContentLoadState = ApiState.Failure(throwable.message ?: "Failed"),
                    )
                }
            }
    }

    private fun validateArtistId() {
        uiState.value.homeContentLoadState.getSuccessDataOrNull()?.let { content ->
            val diff = content.myGroupItems.any { item ->
                item.artistId != content.myGroupItems.first().artistId
            }

            if (diff) {
                updateState { copy(artistIdToNull = true) }
            }
        }
    }
}
