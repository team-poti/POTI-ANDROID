package com.poti.android.presentation.party.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.presentation.party.detail.component.PartyDetailContent
import com.poti.android.presentation.party.detail.component.PartyDetailHeaderInfo
import com.poti.android.presentation.party.detail.component.PartyParticipantsInfo
import com.poti.android.presentation.party.detail.component.PartyUploaderInfo

@Composable
fun PartyDetailRoute(
    onPopBackStack: () -> Unit,
    onNavigateToJoin: () -> Unit,
    onNavigateToProfile: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PartyDetailViewModel = hiltViewModel(),
) {
    PartyDetailScreen(
        partyDetail = dummyPartyDetail,
        onBackClick = onPopBackStack,
        onJoinClick = onNavigateToJoin,
        onUploaderClick = onNavigateToProfile,
        modifier = modifier,
    )
}

@Composable
private fun PartyDetailScreen(
    partyDetail: PartyDetail,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit,
    onUploaderClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.party_detail_title, partyDetail.userSummary.nickname),
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = stringResource(R.string.party_detail_join_party),
                onClick = onJoinClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            HorizontalPager(
                state = rememberPagerState(pageCount = { partyDetail.images.size }),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(375f / 268f),
            ) { page ->
                AsyncImage(
                    model = partyDetail.images[page].imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            PartyDetailHeaderInfo(
                partyDetail = partyDetail,
                onLikeClick = {},
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp), vertical = 20.dp),
            )

            PotiDivider(
                styleType = PotiDividerStyle.SMALL,
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp)),
            )

            PartyDetailContent(
                partyDetail = partyDetail,
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp), vertical = 20.dp),
            )

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            PartyUploaderInfo(
                userSummary = partyDetail.userSummary,
                onClick = onUploaderClick,
                modifier = Modifier.padding(start = screenWidthDp(16.dp), top = 20.dp, end = screenWidthDp(4.dp)),
            )

            PotiDivider(
                styleType = PotiDividerStyle.SMALL,
                modifier = Modifier.padding(horizontal = screenWidthDp(16.dp), vertical = 24.dp),
            )

            PartyParticipantsInfo(
                partyDetail = partyDetail,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(horizontal = screenWidthDp(16.dp)),
            )

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            Text(
                text = stringResource(R.string.party_detail_announcement),
                style = PotiTheme.typography.caption12m,
                color = PotiTheme.colors.gray800,
                modifier = Modifier
                    .padding(horizontal = screenWidthDp(16.dp), vertical = 16.dp)
                    .padding(bottom = 40.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PartyDetailScreenPreview() {
    PotiTheme {
        PartyDetailScreen(
            partyDetail = dummyPartyDetail,
            onBackClick = {},
            onJoinClick = {},
            onUploaderClick = {},
        )
    }
}
