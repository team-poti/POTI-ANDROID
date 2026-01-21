package com.poti.android.presentation.party.goodsfilter

import androidx.lifecycle.SavedStateHandle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.HomeRepository
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsCategoryViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<GoodsCategoryUiState, GoodsCategoryUiIntent, GoodsCategoryUiEffect>(
            initialState = GoodsCategoryUiState(),
        ) {
        private val artistId: Long = checkNotNull(savedStateHandle["artistId"])

        override fun processIntent(intent: GoodsCategoryUiIntent) {
            when (intent) {
                GoodsCategoryUiIntent.OnBackClick -> sendEffect(GoodsCategoryUiEffect.NavigateBack)
                GoodsCategoryUiIntent.OnFloatingClick -> sendEffect(GoodsCategoryUiEffect.NavigateToPartyCreate)
                GoodsCategoryUiIntent.OnSortFilterClick -> {
                    // TODO: [예림] 정렬 바텀시트
                }
                GoodsCategoryUiIntent.OnCardClick -> sendEffect(GoodsCategoryUiEffect.NavigateToGoodsFilter)
            }
        }

        init {
            loadGoodsCategoryList()
        }

        private fun loadGoodsCategoryList() = launchScope {
            updateState { copy(goodsCategoryLoadState = ApiState.Loading) }

            homeRepository.getGoodsCategoryList(
                page = 0,
                size = 10,
                sort = "LATEST", // TODO
                artistId = artistId,
            )
                .onSuccess { goodsCategory ->
                    updateState {
                        copy(goodsCategoryLoadState = ApiState.Success(goodsCategory))
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(
                            goodsCategoryLoadState = ApiState.Failure(
                                throwable.message ?: "Failed to load goods category",
                            ),
                        )
                    }
                }
        }
    }
