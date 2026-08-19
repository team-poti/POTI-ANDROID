package com.poti.android.presentation.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            containerColor = PotiTheme.colors.gray100,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PotiTheme.colors.gray100)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PotiTheme.colors.white)
                    .padding(horizontal = 12.dp)
                    .padding(top = 67.dp, bottom = 47.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserProfile(
                    imageUrl = userProfile.profileImageUrl,
                    nickname = userProfile.nickname,
                )

                RatingBadge(
                    rating = userProfile.ratingAvg.toString(),
                )
            }

            UserInfo(
                activityMessage = userProfile.activityMessage,
                joinedAt = userProfile.joinedAt,
                modifier = Modifier.fillMaxWidth(),
            )

            // 타인의 모집/참여 내역은 조회할 수 없으므로 onItemClick을 전달하지 않는다.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // TODO: [천민재] 변경된 디자인에는 참여 내역 카드가 추가되었으나,
                //  프로필 API(ProfileResponse)가 아직 participationSummary를 내려주지 않는다.
                //  서버 반영 후 UserProfile.participationSummary로 교체할 것.
                HistorySummaryCard(
                    title = stringResource(R.string.user_history_participate),
                    summary = HistorySummary(total = 0, inProgress = 0, completed = 0),
                    modifier = Modifier.weight(1f),
                )

                HistorySummaryCard(
                    title = stringResource(R.string.user_history_recruit),
                    summary = userProfile.recruitSummary,
                    modifier = Modifier.weight(1f),
                )
            }
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
