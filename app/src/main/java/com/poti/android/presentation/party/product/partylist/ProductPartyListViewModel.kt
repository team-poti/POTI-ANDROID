package com.poti.android.presentation.party.product.partylist

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.artist.Member
import com.poti.android.domain.usecase.artist.GetMembersUseCase
import com.poti.android.domain.usecase.party.GetProductPartyListUseCase
import com.poti.android.presentation.party.product.navigation.ProductRoute
import com.poti.android.presentation.party.product.partylist.model.ProductPartyListUiEffect
import com.poti.android.presentation.party.product.partylist.model.ProductPartyListUiIntent
import com.poti.android.presentation.party.product.partylist.model.ProductPartyListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductPartyListViewModel @Inject constructor(
    private val getMembersUseCase: GetMembersUseCase,
    private val getProductPartyListUseCase: GetProductPartyListUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProductPartyListUiState, ProductPartyListUiIntent, ProductPartyListUiEffect>(
        initialState = ProductPartyListUiState(),
    ) {
    private val artistId: Long = savedStateHandle.toRoute<ProductRoute.ProductPartyList>().artistId
    private val title: String = savedStateHandle.toRoute<ProductRoute.ProductPartyList>().title

    init {
        fetchArtistMembers()
        loadPartyList()
    }

    override fun processIntent(intent: ProductPartyListUiIntent) {
        when (intent) {
            ProductPartyListUiIntent.LoadProductPartyList -> loadPartyList()
            ProductPartyListUiIntent.OnBackClick -> sendEffect(ProductPartyListUiEffect.NavigateBack)
            ProductPartyListUiIntent.OnFloatingClick -> sendEffect(
                ProductPartyListUiEffect.NavigateToPartyCreate(
                    artistName = uiState.value.cachedSubTitle,
                    productName = title,
                ),
            )
            is ProductPartyListUiIntent.OnPartyClick -> sendEffect(ProductPartyListUiEffect.NavigateToPartyDetail(intent.partyId))
            ProductPartyListUiIntent.OnMemberFilterClick -> {
                refreshMemberSelectBottomSheet()
                updateState { copy(isMemberFilterBottomSheetVisible = true) }
            }

            is ProductPartyListUiIntent.OnMemberSelect -> {
                onBottomSheetMemberChanged(intent.member)
            }

            ProductPartyListUiIntent.OnSortFilterClick -> {
                updateState { copy(isSortFilterBottomSheetVisible = true) }
            }

            ProductPartyListUiIntent.CloseSortFilterBottomSheet -> {
                updateState { copy(isSortFilterBottomSheetVisible = false) }
            }

            is ProductPartyListUiIntent.OnSortSelect -> {
                updateState { copy(partySortType = intent.sort, isSortFilterBottomSheetVisible = false) }
                loadPartyList()
            }

            ProductPartyListUiIntent.CloseMemberFilterBottomSheet -> updateState { copy(isMemberFilterBottomSheetVisible = false) }

            ProductPartyListUiIntent.OnMemberFilterDone -> saveSelectedMember()

            ProductPartyListUiIntent.OnMemberFilterRefresh -> {
                if (uiState.value.bottomSheetSelectedMembers.isNotEmpty()) {
                    clearSelectedMembers()
                }
            }
        }
    }

    private fun loadPartyList() = launchScope {
        val currentState = uiState.value
        val sort = currentState.partySortType.request
        val memberIds = if (currentState.selectedMembers.isNotEmpty()) {
            currentState.selectedMembers.map { it.memberId }
        } else {
            null
        }

        updateState { copy(productPartyListInfo = ApiState.Loading) }

        getProductPartyListUseCase(
            page = 0,
            size = 100,
            title = title,
            artistId = artistId,
            sort = sort,
            memberIds = memberIds,
        ).onSuccess { partyList ->
            updateState {
                copy(
                    productPartyListInfo = ApiState.Success(partyList),
                    cachedTitle = partyList.partyTitle,
                    cachedSubTitle = partyList.artistName,
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

        getMembersUseCase(artistId)
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
        updateState {
            copy(
                bottomSheetSelectedMembers = selectedMembers,
                isMemberBottomSheetToucehd = false,
            )
        }
    }

    private fun clearSelectedMembers() {
        updateState { copy(bottomSheetSelectedMembers = emptyList(), isMemberBottomSheetToucehd = true) }
    }

    private fun saveSelectedMember() {
        val newSelectedMembers = uiState.value.displayMembers.filter { it in uiState.value.bottomSheetSelectedMembers }

        updateState { copy(selectedMembers = newSelectedMembers) }

        loadPartyList()
    }

    private fun onBottomSheetMemberChanged(member: Member) {
        val selectedMembers = uiState.value.bottomSheetSelectedMembers
        val newSelectedMembers = if (member in selectedMembers) selectedMembers - member else selectedMembers + member
        updateState { copy(bottomSheetSelectedMembers = newSelectedMembers, isMemberBottomSheetToucehd = true) }
    }
}
