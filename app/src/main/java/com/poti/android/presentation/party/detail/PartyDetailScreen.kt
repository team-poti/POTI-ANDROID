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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.common.extension.copyToClipboard
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.extension.shareText
import com.poti.android.core.common.extension.shareTextToX
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.modal.PotiSmallModal
import com.poti.android.core.designsystem.component.navigation.PotiBottomButton
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.share.KakaoShareManager
import com.poti.android.data.mock.UiMockData
import com.poti.android.domain.model.party.PartyDetail
import com.poti.android.presentation.party.detail.component.ParticipantGuidelines
import com.poti.android.presentation.party.detail.component.PartyDetailContent
import com.poti.android.presentation.party.detail.component.PartyDetailHeaderInfo
import com.poti.android.presentation.party.detail.component.PartyJoinBottomSheet
import com.poti.android.presentation.party.detail.component.PartyParticipantsInfo
import com.poti.android.presentation.party.detail.component.PartyShareBottomSheet
import com.poti.android.presentation.party.detail.component.PartyShareButton
import com.poti.android.presentation.party.detail.component.PartyUploaderInfo
import com.poti.android.presentation.party.detail.model.PartyDetailEffect
import com.poti.android.presentation.party.detail.model.PartyDetailIntent
import com.poti.android.presentation.party.detail.model.PartyDetailJoinButtonState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyDetailRoute(
    onPopBackStack: () -> Unit,
    onNavigateToJoin: () -> Unit,
    onNavigateToProfile: (Long) -> Unit,
    onReload: (Long) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: PartyDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val shareChooserTitle = stringResource(R.string.party_detail_share_chooser_title)

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            PartyDetailEffect.NavigateBack -> onPopBackStack()
            PartyDetailEffect.NavigateToJoin -> onNavigateToJoin()
            is PartyDetailEffect.NavigateToProfile -> onNavigateToProfile(effect.userId)
            is PartyDetailEffect.ReloadDetail -> onReload(effect.partyId)
            is PartyDetailEffect.ShareToSystem -> context.shareText(shareChooserTitle, effect.shareText)
            is PartyDetailEffect.ShareToKakao -> KakaoShareManager.sharePartyDetail(context, effect.content)
            is PartyDetailEffect.ShareToX -> context.shareTextToX(effect.shareText)
            is PartyDetailEffect.CopyLink -> context.copyToClipboard(effect.link)
            PartyDetailEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    if (uiState.showLoginRequiredDialog) {
        PotiSmallModal(
            onDismissRequest = { viewModel.processIntent(PartyDetailIntent.OnLoginRequiredDismiss) },
            title = stringResource(R.string.login_required_title),
            text = stringResource(R.string.login_required_join),
            dismissBtnText = stringResource(R.string.login_required_dismiss),
            confirmBtnText = stringResource(R.string.login_required_confirm),
            onDismissBtnClick = { viewModel.processIntent(PartyDetailIntent.OnLoginRequiredDismiss) },
            onConfirmBtnClick = { viewModel.processIntent(PartyDetailIntent.OnLoginRequiredConfirm) },
        )
    }

    if (uiState.showJoinBottomSheet) {
        PartyJoinBottomSheet(
            uiState = uiState,
            onMemberSelect = { viewModel.processIntent(PartyDetailIntent.OnMemberSelect(it)) },
            onMemberRemove = { viewModel.processIntent(PartyDetailIntent.OnMemberRemove(it)) },
            onDeliverySelect = { viewModel.processIntent(PartyDetailIntent.OnDeliverySelect(it)) },
            onDismissRequest = { viewModel.processIntent(PartyDetailIntent.OnDismissBottomSheet) },
            onNextClick = { viewModel.processIntent(PartyDetailIntent.OnOptionNextClick) },
        )
    }

    if (uiState.showShareBottomSheet) {
        PartyShareBottomSheet(
            onDismiss = { viewModel.processIntent(PartyDetailIntent.OnDismissShareBottomSheet) },
            onCopyLinkClick = { viewModel.processIntent(PartyDetailIntent.OnCopyLinkClick) },
            onKakaoShareClick = { viewModel.processIntent(PartyDetailIntent.OnKakaoShareClick) },
            onXShareClick = { viewModel.processIntent(PartyDetailIntent.OnXShareClick) },
            onSystemShareClick = { viewModel.processIntent(PartyDetailIntent.OnSystemShareClick) },
        )
    }

    uiState.partyDetail.onSuccess { partyDetail ->
        PartyDetailScreen(
            partyDetail = partyDetail,
            joinButtonState = uiState.detailJoinButtonState,
            onBackClick = { viewModel.processIntent(PartyDetailIntent.OnBackClick) },
            onJoinClick = { viewModel.processIntent(PartyDetailIntent.OnDetailJoinClick) },
            onUploaderClick = { viewModel.processIntent(PartyDetailIntent.OnUploaderClick(it)) },
            onShareClick = { viewModel.processIntent(PartyDetailIntent.OnShareClick) },
            modifier = modifier,
        )
    }
}

@Composable
private fun PartyDetailScreen(
    partyDetail: PartyDetail,
    joinButtonState: PartyDetailJoinButtonState,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit,
    onUploaderClick: (Long) -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.party_detail_title, partyDetail.uploader.nickname),
            )
        },
        bottomBar = {
            PotiBottomButton(
                text = when (joinButtonState) {
                    PartyDetailJoinButtonState.MY_POST -> stringResource(R.string.party_detail_join_party_my_post)
                    PartyDetailJoinButtonState.ALREADY_JOINED -> stringResource(R.string.party_detail_join_party_already_joined)
                    PartyDetailJoinButtonState.CLOSED -> stringResource(R.string.party_detail_join_party_closed)
                    PartyDetailJoinButtonState.JOIN,
                    PartyDetailJoinButtonState.DISABLED,
                    -> stringResource(R.string.party_detail_join_party)
                },
                onClick = onJoinClick,
                enabled = joinButtonState == PartyDetailJoinButtonState.JOIN,
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            )

            PotiDivider(
                styleType = PotiDividerStyle.SMALL,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            PartyDetailContent(
                partyDetail = partyDetail,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            )

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            PartyUploaderInfo(
                userSummary = partyDetail.uploader,
                onClick = onUploaderClick,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, end = 4.dp),
            )

            PotiDivider(
                styleType = PotiDividerStyle.SMALL,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            )

            PartyParticipantsInfo(
                partyDetail = partyDetail,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(horizontal = 16.dp),
            )

            PotiDivider(styleType = PotiDividerStyle.LARGE)

            PartyShareButton(onClick = onShareClick)

            ParticipantGuidelines()
        }
    }
}

@Preview
@Composable
private fun PartyDetailScreenPreview() {
    PotiTheme {
        PartyDetailScreen(
            partyDetail = UiMockData.partyDetail,
            joinButtonState = PartyDetailJoinButtonState.JOIN,
            onBackClick = {},
            onJoinClick = {},
            onUploaderClick = {},
            onShareClick = {},
        )
    }
}
