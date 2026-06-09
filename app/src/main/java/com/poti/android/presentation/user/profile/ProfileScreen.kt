package com.poti.android.presentation.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.presentation.user.component.HistorySummaryCard
import com.poti.android.presentation.user.component.RatingBadge
import com.poti.android.presentation.user.component.UserInfo
import com.poti.android.presentation.user.component.UserProfile
import com.poti.android.presentation.user.profile.model.ProfileUiEffect

@Composable
fun ProfileScreenRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            ProfileUiEffect.NavigateBack -> onPopBackStack()
        }
    }

    uiState.userProfileLoadState.onSuccess { userProfile ->
        ProfileScreen(
            userProfile = userProfile,
            onBackClick = onPopBackStack,
            modifier = modifier,
        )
    }
}

@Composable
private fun ProfileScreen(
    userProfile: UserProfile,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserProfile(
                imageUrl = userProfile.profileImageUrl,
                nickname = userProfile.nickname,
                email = userProfile.email,
            )

            RatingBadge(
                rating = userProfile.ratingAvg.toString(),
            )

            UserInfo(
                activityMessage = userProfile.activityMessage,
                joinedAt = userProfile.joinedAt,
                modifier = Modifier.fillMaxWidth(),
            )

            HistorySummaryCard(
                title = stringResource(R.string.user_history_recruit),
                summary = userProfile.recruitSummary,
                onItemClick = { type -> },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    PotiTheme {
        ProfileScreen(
            userProfile = UserProfile(
                userId = 1L,
                email = "akkma@app.jam",
                nickname = "분철의 악마",
                profileImageUrl = "",
                ratingAvg = 4.8,
                activityMessage = "최근 3일 이내 활동",
                joinedAt = "2025-12-28",
                hasFavoriteArtist = true,
                recruitSummary = HistorySummary(
                    total = 7,
                    inProgress = 2,
                    completed = 5,
                ),
            ),
            onBackClick = { },
            modifier = Modifier,
        )
    }
}
