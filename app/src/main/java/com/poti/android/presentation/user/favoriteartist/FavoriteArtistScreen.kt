package com.poti.android.presentation.user.favoriteartist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.constant.ExternalLinks
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiArtistButton
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.data.mock.UiMockData
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiEffect
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiIntent
import com.poti.android.presentation.user.favoriteartist.model.FavoriteArtistUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoriteArtistRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteArtistViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            FavoriteArtistUiEffect.NavigateBack -> onPopBackStack()
            FavoriteArtistUiEffect.SaveSuccess -> onPopBackStack()
        }
    }

    FavoriteArtistScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(FavoriteArtistUiIntent.OnBackClick) },
        onArtistClick = { artistId ->
            viewModel.processIntent(FavoriteArtistUiIntent.OnArtistSelect(artistId))
        },
        onSaveClick = { viewModel.processIntent(FavoriteArtistUiIntent.OnSaveClick) },
        onInquiryClick = { uriHandler.openUri(ExternalLinks.INQUIRY) },
        modifier = modifier,
    )
}

@Composable
private fun FavoriteArtistScreen(
    uiState: FavoriteArtistUiState,
    onBackClick: () -> Unit,
    onArtistClick: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onInquiryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.favorite_artist_title),
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = stringResource(R.string.action_button_done),
                onClick = onSaveClick,
                enabled = uiState.isSaveEnabled,
            )
        },
    ) { innerPadding ->
        uiState.artists.onSuccess { artists ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp),
            ) {
                items(
                    items = artists,
                    key = { artist -> artist.artistId },
                ) { artist ->
                    PotiArtistButton(
                        imageUrl = artist.logoImageUrl,
                        text = artist.name,
                        selected = (uiState.selectedArtistId == artist.artistId),
                        onClick = { onArtistClick(artist.artistId) },
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    InquiryRow(
                        onInquiryClick = onInquiryClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 72.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InquiryRow(
    onInquiryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .noRippleClickable(onClick = onInquiryClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.favorite_artist_empty_group),
            color = PotiTheme.colors.gray800,
            style = PotiTheme.typography.body14m,
        )

        Text(
            text = stringResource(R.string.user_inquiry_action),
            color = PotiTheme.colors.poti800,
            style = PotiTheme.typography.body14sb,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteArtistScreenPreview() {
    PotiTheme {
        FavoriteArtistScreen(
            uiState = FavoriteArtistUiState(
                artists = ApiState.Success(UiMockData.artists.toImmutableList()),
                selectedArtistId = 1L,
            ),
            onBackClick = {},
            onArtistClick = {},
            onSaveClick = {},
            onInquiryClick = {},
        )
    }
}
