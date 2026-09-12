package com.poti.android.presentation.party.home

import com.poti.android.core.analytics.AnalyticsEvent
import com.poti.android.core.analytics.AnalyticsEventProperty
import com.poti.android.core.analytics.AnalyticsValue
import com.poti.android.core.analytics.EventTracker
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.auth.IsGuestUseCase
import com.poti.android.domain.usecase.home.GetHomeContentUseCase
import com.poti.android.presentation.party.home.model.HomeUiEffect
import com.poti.android.presentation.party.home.model.HomeUiEffect.*
import com.poti.android.presentation.party.home.model.HomeUiIntent
import com.poti.android.presentation.party.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeContentUseCase: GetHomeContentUseCase,
    private val isGuestUseCase: IsGuestUseCase,
    private val eventTracker: EventTracker,
) : BaseViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(
        initialState = HomeUiState(),
    ) {
    override fun processIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.OnSearchClick -> sendEffect(NavigateToPartySearch)
            HomeUiIntent.OnFloatingClick -> handleFloatingClick()
            is HomeUiIntent.OnMyArtistCategoryClick -> {
                trackHomeSectionMoreClicked(AnalyticsValue.RECOMMENDED)
                sendEffect(NavigateToMyArtistCategory(if (uiState.value.artistIdToNull) null else intent.artistId))
            }
            is HomeUiIntent.OnProductCardClick -> {
                eventTracker.track(
                    eventName = AnalyticsEvent.GOODS_CARD_CLICKED,
                    properties = mapOf(
                        AnalyticsEventProperty.GROUP_ID to intent.artistId.toString(),
                        AnalyticsEventProperty.GOODS_ID to intent.goodsId.toString(),
                        AnalyticsEventProperty.HOME_SECTION to intent.homeSection,
                        AnalyticsEventProperty.SOURCE to AnalyticsValue.HOME,
                        AnalyticsEventProperty.POSITION to intent.position,
                    ),
                )
                sendEffect(NavigateToGoodsPartyList(intent.goodsId, intent.artistId, intent.title))
            }
            HomeUiIntent.LoadHomeContent -> loadHomeContent()
            HomeUiIntent.OnOtherProductCategoryClick -> {
                trackHomeSectionMoreClicked(AnalyticsValue.DISCOVER)
                sendEffect(NavigateToOtherProductCategory)
            }
            HomeUiIntent.OnAlarmClick -> {
                if (!isGuestUseCase()) sendEffect(NavigateToAlarmList)
            }
            HomeUiIntent.OnLoginRequiredConfirm -> handleLoginRequiredConfirm()
            HomeUiIntent.OnLoginRequiredDismiss -> updateState { copy(showLoginRequiredDialog = false) }
        }
    }

    private fun trackHomeSectionMoreClicked(homeSection: String) {
        eventTracker.track(
            eventName = AnalyticsEvent.HOME_SECTION_MORE_CLICKED,
            properties = mapOf(AnalyticsEventProperty.HOME_SECTION to homeSection),
        )
    }

    private fun handleFloatingClick() {
        if (isGuestUseCase()) {
            updateState { copy(showLoginRequiredDialog = true) }
        } else {
            sendEffect(NavigateToPartyCreate)
        }
    }

    private fun handleLoginRequiredConfirm() {
        updateState { copy(showLoginRequiredDialog = false) }
        sendEffect(NavigateToLogin)
    }

    init {
        processIntent(HomeUiIntent.LoadHomeContent)
    }

    private fun loadHomeContent() = launchScope {
        getHomeContentUseCase()
            .onSuccess { homeContent ->
                eventTracker.track(
                    eventName = AnalyticsEvent.HOME_VIEWED,
                    properties = buildMap {
                        put(
                            AnalyticsEventProperty.CONTENT_TYPE,
                            if (homeContent.mainArtistId == null) AnalyticsValue.ALL else AnalyticsValue.FAVORITE_GROUP,
                        )
                        homeContent.mainArtistId?.let {
                            put(AnalyticsEventProperty.FAVORITE_GROUP_ID, it.toString())
                        }
                    },
                )
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
