package com.poti.android.presentation.party.create

import androidx.lifecycle.viewModelScope
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.usecase.artist.GetMembersWithPriceUseCase
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.party.CreatePartyUseCase
import com.poti.android.domain.usecase.party.GetDeliveryOptionsUseCase
import com.poti.android.domain.usecase.party.SearchArtistUseCase
import com.poti.android.domain.usecase.party.SearchProductUseCase
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartyCreateViewModel @Inject constructor(
    private val getMembersWithPriceUseCase: GetMembersWithPriceUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val createPartyUseCase: CreatePartyUseCase,
    private val searchArtistUseCase: SearchArtistUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val getDeliveryOptionsUseCase: GetDeliveryOptionsUseCase,
) : BaseViewModel<CreateUiState, CreateUiIntent, CreateUiEffect>(
        initialState = CreateUiState(),
    ) {
    override fun processIntent(intent: CreateUiIntent) {
        when (intent) {
            is CreateUiIntent.OnAccountNumberChange -> {}
            is CreateUiIntent.OnArtistSelect -> {}
            is CreateUiIntent.OnBankChange -> {}
            CreateUiIntent.OnCreateClick -> {}
            is CreateUiIntent.OnDeadlineChange -> {}
            is CreateUiIntent.OnDeliverySelect -> {}
            is CreateUiIntent.OnDescriptionChange -> {}
            is CreateUiIntent.OnImagesChanged -> {}
            CreateUiIntent.OnMemberEditClick -> {}
            is CreateUiIntent.OnMembersSelect -> {}
            is CreateUiIntent.OnPriceChange -> {}
            is CreateUiIntent.OnProductChange -> {}
            is CreateUiIntent.OnProductSelect -> {}
            CreateUiIntent.OnSearchClick -> {}
            CreateUiIntent.OnBackClick -> {}
        }
    }

    init {
        viewModelScope.launch {
            getDeliveryOptions()
            delay(300)
            getMembers()
            delay(300)
            searchArtist()
            delay(300)
            searchProduct()
            delay(300)
            createPost()
        }
    }

    private fun getDeliveryOptions() {
        viewModelScope.launch {
            getDeliveryOptionsUseCase()
                .onSuccess {
                    updateState {
                        copy(
                            deliveryOptionsState = ApiState.Success(it.toPersistentList()),
                        )
                    }
                }
        }
    }

    private fun getMembers() {
        viewModelScope.launch {
            getMembersWithPriceUseCase(1L)
                .onSuccess {
                    updateState {
                        copy(
                            memberOptionsState = ApiState.Success(it.toPersistentList()),
                        )
                    }
                }
        }
    }

    private fun searchArtist() {
        viewModelScope.launch {
            searchArtistUseCase("엔시티")
        }
    }

    private fun searchProduct() {
        viewModelScope.launch {
            searchProductUseCase(1, "하이")
        }
    }

    private fun createPost() {
        viewModelScope.launch {
            val memberOption = uiState.value.memberOptionsState.getSuccessDataOrNull()!![0]
            val newMemberOption = memberOption.copy(price = "1000")

            val shippingOption = uiState.value.deliveryOptionsState.getSuccessDataOrNull()!![0]

            createPartyUseCase(
                artistId = 1,
                product = "하이",
                description = "하이",
                deadline = "2026-01-22",
                bank = "기업은행",
                accountNumber = "01066152589",
                imageUrls = listOf("https://mblogthumb-phinf.pstatic.net/20150829_137/deuxiemevie7_14408303505758j79p_JPEG/tumblr_nqvhsexuKb1s8b8vuo1_1280.jpg?type=w420"),
                options = listOf(newMemberOption),
                shippings = listOf(shippingOption),
            )
        }
    }
}
