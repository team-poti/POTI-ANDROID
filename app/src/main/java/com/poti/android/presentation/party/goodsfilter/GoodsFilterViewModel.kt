package com.poti.android.presentation.party.goodsfilter

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.repository.ArtistRepository
import com.poti.android.domain.repository.PartyRepository
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiEffect
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiIntent
import com.poti.android.presentation.party.goodsfilter.model.GoodsFilterUiState
import com.poti.android.presentation.party.goodsfilter.navigation.GoodsRoute.GoodsPartyList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsFilterViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
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
                refreshMemberSelectBottomSheet()
                updateState { copy(isMemberFilterBottomSheetVisible = true) }
            }

            is GoodsFilterUiIntent.OnMemberSelect -> {
                onBottomSheetMemberChanged(intent.index)
            }

            GoodsFilterUiIntent.OnSortFilterClick -> {
                updateState { copy(isSortFilterBottomSheetVisible = true) }
            }

            GoodsFilterUiIntent.CloseSortFilterBottomSheet -> {
                updateState { copy(isSortFilterBottomSheetVisible = false) }
            }

            is GoodsFilterUiIntent.OnSortSelect -> {
                updateState { copy(goodsPartySortType = intent.sort, isSortFilterBottomSheetVisible = false) }
                loadPartyList()
            }

            GoodsFilterUiIntent.CloseMemberFilterBottomSheet -> updateState { copy(isMemberFilterBottomSheetVisible = false) }

            GoodsFilterUiIntent.OnMemberFilterDone -> saveSelectedMember()

            GoodsFilterUiIntent.OnMemberFilterRefresh -> refreshMemberSelectBottomSheet()
        }
    }

    private fun loadPartyList() = launchScope {
        val currentState = uiState.value
        val sort = currentState.goodsPartySortType.request
        val memberIds = if (currentState.selectedMembers.isNotEmpty()) {
            currentState.selectedMembers.map { it.memberId }
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
                    cachedTitle = partyList.partyTitle,
                    cacheedSubTitle = partyList.artistName,
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

        artistRepository.getMemberList(artistId)
            .onSuccess {
                updateState {
                    copy(
                        membersLoadState = ApiState.Success(it),
                        displayMembers = it,
                    )
                }
            }
            .onFailure { e ->
                updateState { copy(membersLoadState = ApiState.Failure(e.message ?: "Failed")) }
            }
    }

    private fun refreshMemberSelectBottomSheet() {
        val newSelectedIndices = uiState.value.displayMembers.mapIndexed { index, member ->
            if (member in uiState.value.selectedMembers) index else null
        }
            .filterNotNull()
            .toSet()

        updateState {
            copy(
                bottomSheetSelectedMembersIdices = newSelectedIndices,
                isMemberBottomSheetToucehd = false,
            )
        }
    }

    private fun saveSelectedMember() {
        val newSelectedMembers = uiState.value.displayMembers.mapIndexedNotNull { index, member ->
            if (index in uiState.value.bottomSheetSelectedMembersIdices) member else null
        }

        updateState { copy(selectedMembers = newSelectedMembers) }

        loadPartyList()
    }

    private fun onBottomSheetMemberChanged(index: Int) {
        val selectedIndices = uiState.value.bottomSheetSelectedMembersIdices
        val newSelectedIndices = if (index in selectedIndices) selectedIndices - index else selectedIndices + index
        updateState { copy(bottomSheetSelectedMembersIdices = newSelectedIndices, isMemberBottomSheetToucehd = true) }
    }
}
