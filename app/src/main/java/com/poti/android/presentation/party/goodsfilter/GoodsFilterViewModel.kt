package com.poti.android.presentation.party.goodsfilter

import androidx.lifecycle.SavedStateHandle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsFilterViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    savedStateHandle: SavedStateHandle,
) :
    BaseViewModel<GoodsFilterUiState, GoodsFilterUiIntent, GoodsFilterUiEffect>(
            initialState = GoodsFilterUiState(),
        ) {
        private val artistId: Long = checkNotNull(savedStateHandle["artistId"])

        init {
            loadPartyList()
        }

        override fun processIntent(intent: GoodsFilterUiIntent) {
            when (intent) {
                GoodsFilterUiIntent.LoadGoodsPots -> loadPartyList()
                GoodsFilterUiIntent.OnBackClick -> sendEffect(GoodsFilterUiEffect.NavigateBack)
                GoodsFilterUiIntent.OnFloatingClick -> sendEffect(GoodsFilterUiEffect.NavigateToPartyCreate)
                is GoodsFilterUiIntent.OnPartyClick ->
                    sendEffect(GoodsFilterUiEffect.NavigateToPartyDetail(intent.partyId))
                GoodsFilterUiIntent.OnMemberFilterClick -> {
                    // TODO: [예림] 바텀시트 open
                }
                is GoodsFilterUiIntent.OnMembersSelect -> {
                    updateState { copy(selectedMembers = intent.members) }
                }
                GoodsFilterUiIntent.OnSortFilterClick -> {
                    // TODO: [예림] 바텀시트 open
                }
                is GoodsFilterUiIntent.OnSortSelect -> {
                    updateState { copy(goodsPartySortType = intent.sort) }
                }
            }
        }

        private fun loadPartyList() = launchScope {
            val sort = uiState.value.goodsPartySortType.request
            val memberIds = uiState.value.selectedMemberIds

            updateState { copy(partyListInfo = ApiState.Loading) }

            partyRepository.getPartyList(
                page = 0,
                size = 10,
                title = "", // TODO 타이틀 받아오기
                artistId = artistId,
                sort = sort,
                memberIds = memberIds, // TODO 바텀시트 연결
            )
                .onSuccess { partyList ->
                    updateState {
                        copy(
                            partyListInfo = ApiState.Success(partyList),
                            membersLoadState = ApiState.Success(emptyList()), // TODO 멤버 API 연결
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState {
                        copy(
                            partyListInfo = ApiState.Failure(
                                throwable.message ?: "Failed",
                            ),
                        )
                    }
                }
        }
    }
