package com.poti.android.presentation.party.product.productcategory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.button.PotiFloatingButton
import com.poti.android.core.designsystem.component.button.PotiSmallButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.party.ProductCategory
import com.poti.android.presentation.party.home.component.GoodsLargeCard
import com.poti.android.presentation.party.product.component.GoodsSortBottomSheet
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiEffect
import com.poti.android.presentation.party.product.productcategory.model.ProductCategoryUiIntent
import com.poti.android.presentation.party.product.productcategory.model.ProductSortType

@Composable
fun ProductCategoryRoute(
    artistId: Long?,
    onPopBackStack: () -> Unit,
    onNavigateToPartyCreate: (Long?) -> Unit,
    onNavigateToProductPartyList: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            ProductCategoryUiEffect.NavigateBack -> onPopBackStack()
            is ProductCategoryUiEffect.NavigateToPartyCreate -> onNavigateToPartyCreate(artistId)
            is ProductCategoryUiEffect.NavigateToProductPartyList -> onNavigateToProductPartyList(effect.artistId, effect.title)
        }
    }

    uiState.productCategoryLoadState.onSuccess { goodsCategory ->
        ProductCategoryScreen(
            title = if (viewModel.isMyArtist) stringResource(R.string.home_recommend_goods, goodsCategory.nickname) else stringResource(R.string.home_other_goods),
            isMyArtsit = viewModel.isMyArtist,
            productCategory = goodsCategory,
            selectedSortType = uiState.selectedSortType,
            isSortBottomSheetVisible = uiState.isSortBottomSheetVisible,
            onBackClick = { viewModel.processIntent(ProductCategoryUiIntent.OnBackClick) },
            onFloatingClick = { viewModel.processIntent(ProductCategoryUiIntent.OnFloatingClick) },
            onSortFilterClick = { viewModel.processIntent(ProductCategoryUiIntent.OnSortFilterClick) },
            onSortSelect = { viewModel.processIntent(ProductCategoryUiIntent.OnSortSelected(it)) },
            onSortDismiss = { viewModel.processIntent(ProductCategoryUiIntent.OnSortDismiss) },
            onCardClick = { artistId, title -> viewModel.processIntent(ProductCategoryUiIntent.OnCardClick(artistId, title)) },
            modifier = modifier,
        )
    }
}

@Composable
private fun ProductCategoryScreen(
    title: String,
    isMyArtsit: Boolean,
    productCategory: ProductCategory,
    selectedSortType: ProductSortType,
    isSortBottomSheetVisible: Boolean,
    onBackClick: () -> Unit,
    onFloatingClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onSortSelect: (ProductSortType) -> Unit,
    onSortDismiss: () -> Unit,
    onCardClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isSortBottomSheetVisible) {
        GoodsSortBottomSheet(
            selectedSortType = selectedSortType,
            onSelect = onSortSelect,
            onDismissRequest = onSortDismiss,
        )
    }

    Box(
        modifier = modifier,
    ) {
        Column {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = title,
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 76.dp,
                ),
            ) {
                item {
                    if (isMyArtsit) {
                        PotiSmallButton(
                            text = stringResource(selectedSortType.displayRes),
                            onClick = onSortFilterClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.End),
                        )
                    } else {
                        Spacer(Modifier.height(12.dp))
                    }
                }

                items(productCategory.groupItems) { groupItem ->
                    GoodsLargeCard(
                        imageUrl = groupItem.postImage,
                        artist = groupItem.artist,
                        title = groupItem.postTitle,
                        partyCount = groupItem.postCount,
                        tag = groupItem.tag,
                        onClick = { id, title -> onCardClick(groupItem.artistId, groupItem.postTitle) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        artistId = groupItem.artistId,
                    )
                }
            }
        }

        PotiFloatingButton(
            onClick = onFloatingClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,
                    bottom = 12.dp,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCategoryScreenPreview() {
    PotiTheme {
        ProductCategoryScreen(
            title = "",
            isMyArtsit = false,
            productCategory = UiMockData.productCategory,
            selectedSortType = ProductSortType.LATEST,
            isSortBottomSheetVisible = false,
            onBackClick = {},
            onFloatingClick = {},
            onSortFilterClick = {},
            onSortSelect = {},
            onSortDismiss = {},
            onCardClick = { _, _ -> },
        )
    }
}
