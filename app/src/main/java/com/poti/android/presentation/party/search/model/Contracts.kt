package com.poti.android.presentation.party.search.model

import androidx.compose.runtime.Immutable
import com.poti.android.core.base.UiEffect
import com.poti.android.core.base.UiIntent
import com.poti.android.core.base.UiState
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.party.ProductCategory

@Immutable
data class PartySearchUiState(
    val searchText: String = "",
    val productCategoryLoadState: ApiState<ProductCategory> = ApiState.Loading,
) : UiState {}

sealed interface PartySearchUiIntent : UiIntent {
}

sealed interface PartySearch : UiEffect {
}
