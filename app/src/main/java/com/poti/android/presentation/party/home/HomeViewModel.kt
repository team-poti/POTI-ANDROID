package com.poti.android.presentation.party.home

import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.home.Banner
import com.poti.android.domain.model.home.HomeContent
import com.poti.android.presentation.party.home.model.HomeUiEffect
import com.poti.android.presentation.party.home.model.HomeUiIntent
import com.poti.android.presentation.party.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(
    initialState = HomeUiState(),
) {
    override fun processIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.OnFloatingClick -> sendEffect(HomeUiEffect.NavigateToPartyCreate)
            is HomeUiIntent.OnBannerClick -> sendEffect(HomeUiEffect.NavigateToPartyDetail(intent.postId))
            HomeUiIntent.OnMoreClick -> sendEffect(HomeUiEffect.NavigateToGoodsCategory)
            HomeUiIntent.OnCardClick -> sendEffect(HomeUiEffect.NavigateToGoodsPartyList)
            HomeUiIntent.LoadHomeContent -> loadHomeContent()
        }
    }

    init {
        processIntent(HomeUiIntent.LoadHomeContent)
    }

    private fun loadHomeContent() = launchScope {
        updateState {
            copy(
                homeContentLoadState = ApiState.Success(
                    HomeContent(
                        nickname = "포티",
                        banners = listOf(
                            Banner(1, ""),
                            Banner(2, ""),
                            Banner(3, ""),
                        ),
                        myGroupItems = fakeMyGroupItems,
                        otherGroupItems = fakeMyGroupItems,
                    ),
                ),
            )
        }
    }
}
