package com.poti.android.presentation.party.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.getSuccessDataOrNull
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.ActionButtonType
import com.poti.android.core.designsystem.component.button.PotiActionButton
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.ArtistSearchResult
import com.poti.android.presentation.party.create.component.CreateDropdownField
import com.poti.android.presentation.party.create.component.ViewType
import com.poti.android.presentation.party.create.model.CreateUiEffect
import com.poti.android.presentation.party.create.model.CreateUiIntent
import com.poti.android.presentation.party.create.model.CreateUiState

@Composable
fun PartyArtistSelectRoute(
    onPopBackStack: () -> Unit,
    viewModel: PartyCreateViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            CreateUiEffect.NavigateToBack -> onPopBackStack()
            else -> Unit
        }
    }

    PartyArtistSelectScreen(
        uiState = uiState,
        onSearchKeywordChange = { viewModel.processIntent(CreateUiIntent.OnArtistSearchKeywordChange(it)) },
        onArtistSelect = { viewModel.processIntent(CreateUiIntent.OnArtistSelect(it)) },
        onConfirmClick = { viewModel.processIntent(CreateUiIntent.OnBackToCreate) },
        onPopBackStack = { viewModel.processIntent(CreateUiIntent.OnBackToCreate) },
        modifier = modifier,
    )
}

@Composable
private fun PartyArtistSelectScreen(
    uiState: CreateUiState,
    onSearchKeywordChange: (String) -> Unit,
    onArtistSelect: (ArtistSearchResult) -> Unit,
    onConfirmClick: () -> Unit,
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(),
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onPopBackStack,
                title = stringResource(R.string.create_header_artist_search),
            )
        },
        bottomBar = {
            PotiActionButton(
                text = stringResource(R.string.action_button_done),
                onClick = onConfirmClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(16.dp))
                    .padding(top = 4.dp, bottom = 16.dp),
                type = if (uiState.isArtistSelectDoneBtnEnabled) ActionButtonType.SECONDARY_MAIN else ActionButtonType.DEACTIVE_MAIN,
                enabled = uiState.isArtistSelectDoneBtnEnabled,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            CreateDropdownField(
                viewType = ViewType.ARTSIT_SELECT,
                value = uiState.artistSearchKeyword,
                onValueChanged = onSearchKeywordChange,
                searchResults = uiState.artistSearchResultsState.getSuccessDataOrNull() ?: emptyList(),
                resultToString = { it.name },
                onItemClick = { onArtistSelect(it) },
                placeholder = stringResource(R.string.create_placeholder_artist_search),
                selectedString = uiState.selectedArtist?.name ?: "",
                modifier = Modifier.padding(vertical = 12.dp),
                showTrailingIcon = true,
            )

            if (uiState.isArtistSearchResultsEmpty) {
                PotiEmptyStateInline(
                    text = stringResource(R.string.create_message_artist_search_empty_result),
                    modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PartyArtistSelectScreenPreview() {
    PotiTheme {
        PartyArtistSelectScreen(
            uiState = CreateUiState(),
            onSearchKeywordChange = {},
            onArtistSelect = {},
            onConfirmClick = {},
            onPopBackStack = {},
        )
    }
}
