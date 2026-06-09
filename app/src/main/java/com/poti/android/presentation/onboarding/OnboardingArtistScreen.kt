package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiArtistButton
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.onboarding.component.OnboardingScaffold
import com.poti.android.presentation.onboarding.model.OnboardingUiEffect
import com.poti.android.presentation.onboarding.model.OnboardingUiIntent
import com.poti.android.presentation.onboarding.model.OnboardingUiState

@Composable
fun OnboardingArtistRoute(
    onPopBackStack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.NavigateToBack -> onPopBackStack()
            OnboardingUiEffect.NavigateToHome -> onNavigateToHome()
            else -> {}
        }
    }

    OnboardingArtistScreen(
        uiState = uiState,
        onArtistClick = { viewModel.processIntent(OnboardingUiIntent.OnArtistSelect(it)) },
        onPopBackStack = { viewModel.processIntent(OnboardingUiIntent.OnBackClick) },
        onStartClick = { viewModel.processIntent(OnboardingUiIntent.OnStartClick) },
        onSkipClick = { viewModel.processIntent(OnboardingUiIntent.OnSkipClick) },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingArtistScreen(
    uiState: OnboardingUiState,
    onArtistClick: (Long) -> Unit,
    onPopBackStack: () -> Unit,
    onStartClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScaffold(
        currentStep = 3,
        onBackClick = onPopBackStack,
        onNextClick = onStartClick,
        modifier = modifier,
        isButtonVisible = uiState.isButtonVisible,
        onSkip = onSkipClick,
    ) {
        uiState.artists.onSuccess { artists ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 40.dp,
                ),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(R.string.onboarding_artist_label, uiState.nickname),
                        style = PotiTheme.typography.title18sb,
                        color = PotiTheme.colors.black,
                        modifier = Modifier.padding(top = 24.dp, bottom = 26.dp),
                    )
                }

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
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingArtistScreenPreview() {
    PotiTheme {
        OnboardingArtistScreen(
            uiState = OnboardingUiState(),
            onArtistClick = {},
            onPopBackStack = {},
            onStartClick = {},
            onSkipClick = {},
        )
    }
}
