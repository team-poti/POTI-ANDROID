package com.poti.android.presentation.party.goodsfilter

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiState
import com.poti.android.presentation.party.goodsfilter.navigation.GoodsRoute.GoodsPartyList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsFilterViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<GoodsFilterUiState, GoodsFilterUiIntent, GoodsFilterUiEffect>(
        initialState = GoodsFilterUiState(),
    ) {
    private val artistId: Long = savedStateHandle.toRoute<GoodsPartyList>().artistId
    private val title: String = savedStateHandle.toRoute<GoodsPartyList>().title

    init {
        fetchArtistMembers()
        loadPartyList()
    }

    override fun processIntent(intent: GoodsFilterUiIntent) {
        when (intent) {
            GoodsFilterUiIntent.LoadGoodsPots -> loadPartyList()
            GoodsFilterUiIntent.OnBackClick -> sendEffect(GoodsFilterUiEffect.NavigateBack)
            GoodsFilterUiIntent.OnFloatingClick -> sendEffect(GoodsFilterUiEffect.NavigateToPartyCreate)
            is GoodsFilterUiIntent.OnPartyClick -> sendEffect(GoodsFilterUiEffect.NavigateToPartyDetail(intent.partyId))
            GoodsFilterUiIntent.OnMemberFilterClick -> {
                // TODO: [예림] 바텀시트 open
            }

            is GoodsFilterUiIntent.OnMembersSelect -> {
                updateState { copy(selectedMembers = intent.members) }
                loadPartyList()
            }

            GoodsFilterUiIntent.OnSortFilterClick -> {
                // TODO: [예림] 바텀시트 open
            }

            is GoodsFilterUiIntent.OnSortSelect -> {
                updateState { copy(goodsPartySortType = intent.sort) }
                loadPartyList()
            }
        }
    }

    private fun loadPartyList() = launchScope {
        val currentState = uiState.value
        val sort = currentState.goodsPartySortType.request
        val memberIds = if (currentState.selectedMembers.isNotEmpty()) {
            currentState.selectedMembers.map { it.id }
        } else {
            null
        }

        updateState { copy(productPartyListInfo = ApiState.Loading) }

        partyRepository.getProductPartyList(
            page = 0,
            size = 10,
            title = title,
            artistId = artistId,
            sort = sort,
            memberIds = memberIds,
        ).onSuccess { partyList ->
            updateState {
                copy(
                    productPartyListInfo = ApiState.Success(partyList),
                    membersLoadState = ApiState.Success(emptyList()), // TODO 멤버 API 연결
                )
            }
        }.onFailure { throwable ->
            updateState {
                copy(
                    productPartyListInfo = ApiState.Failure(
                        throwable.message ?: "Failed",
                    ),
                )
            }
        }
    }

    private fun fetchArtistMembers() = launchScope {
        updateState { copy(membersLoadState = ApiState.Loading) }

        updateState { copy(membersLoadState = ApiState.Success(emptyList())) }
    }
}
