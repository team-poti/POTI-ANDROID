package com.poti.android.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.artist.Artist
import com.poti.android.presentation.onboarding.component.ArtistItem
import com.poti.android.presentation.onboarding.component.OnboardingScaffold

@Composable
fun OnboardingArtistRoute(
    onPopBackStack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingArtistScreen(
        artists = dummyArtists,
        onPopBackStack = onPopBackStack,
        onNavigateToHome = onNavigateToHome,
        modifier = modifier,
    )
}

@Composable
private fun OnboardingArtistScreen(
    artists: List<Artist>,
    onPopBackStack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isButtonVisible by remember { mutableStateOf(true) }

    OnboardingScaffold(
        currentStep = 3,
        title = stringResource(R.string.onboarding_artist_label, "포티"), // TODO: [지현] 닉네임 연결
        onBackClick = onPopBackStack,
        onNextClick = {
            isButtonVisible = false
            onNavigateToHome()
        },
        modifier = modifier,
        isButtonVisible = isButtonVisible,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = screenWidthDp(16.dp),
                top = 18.dp,
                end = screenWidthDp(16.dp),
                bottom = 40.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            items(
                items = artists,
                key = { artist -> artist.artistId },
            ) { artist ->
                ArtistItem(artist)
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingArtistScreenPreview() {
    PotiTheme {
        OnboardingArtistScreen(
            artists = dummyArtists,
            onPopBackStack = {},
            onNavigateToHome = {},
        )
    }
}
