package com.poti.android.presentation.alarm.list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poti.android.R
import com.poti.android.core.common.extension.onSuccess
import com.poti.android.core.common.extension.toRelativeTime
import com.poti.android.core.common.extension.toast
import com.poti.android.core.common.state.ApiState
import com.poti.android.core.common.util.HandleSideEffects
import com.poti.android.core.designsystem.component.display.PotiDivider
import com.poti.android.core.designsystem.component.display.PotiDividerStyle
import com.poti.android.core.designsystem.component.display.PotiEmptyStateInline
import com.poti.android.core.designsystem.component.display.PotiNoticeItem
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.notification.Notification
import com.poti.android.domain.type.NotificationType
import com.poti.android.presentation.alarm.list.component.AlarmReadButton
import com.poti.android.presentation.alarm.list.model.AlarmListUiEffect
import com.poti.android.presentation.alarm.list.model.AlarmListUiIntent
import com.poti.android.presentation.alarm.list.model.AlarmListUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun AlarmListRoute(
    onPopBackStack: () -> Unit,
    navigateToSetting: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlarmListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AlarmListUiEffect.NavigateBack -> onPopBackStack()
            AlarmListUiEffect.NavigateToSetting -> navigateToSetting()
            is AlarmListUiEffect.OpenDeepLink -> context.openDeepLink(effect.deepLink)
            is AlarmListUiEffect.ShowToast -> context.toast(context.getString(effect.messageRes))
        }
    }

    AlarmListScreen(
        uiState = uiState,
        onBackClick = { viewModel.processIntent(AlarmListUiIntent.OnBackClick) },
        onSettingClick = { viewModel.processIntent(AlarmListUiIntent.OnSettingClick) },
        onAlarmClick = { alarm -> viewModel.processIntent(AlarmListUiIntent.OnAlarmClick(alarm)) },
        onAlarmReadAllClick = { viewModel.processIntent(AlarmListUiIntent.OnAlarmReadAllClick) },
        modifier = modifier,
    )
}

@Composable
private fun AlarmListScreen(
    uiState: AlarmListUiState,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
    onAlarmClick: (Notification) -> Unit,
    onAlarmReadAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PotiHeaderPage(
                onNavigationClick = onBackClick,
                title = stringResource(R.string.alarm_title),
                onTrailingIconClick = onSettingClick,
                trailingIconRes = R.drawable.ic_setting,
            )
        },
        bottomBar = {
            AlarmReadButton(
                onClick = onAlarmReadAllClick,
                enabled = uiState.alarmReadAllEnabled,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 14.dp),
            )
        },
    ) { innerPadding ->
        uiState.alarmsLoadState.onSuccess { alarms ->
            if (alarms.isEmpty()) {
                PotiEmptyStateInline(
                    text = stringResource(R.string.alarm_empty),
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 12.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    items(
                        items = alarms,
                        key = { alarm -> alarm.id },
                    ) { alarm ->
                        Column {
                            PotiDivider(styleType = PotiDividerStyle.SMALL)

                            PotiNoticeItem(
                                title = alarm.title,
                                content = alarm.body,
                                time = alarm.createdAt.toRelativeTime(),
                                isRead = alarm.isRead,
                                onClick = { onAlarmClick(alarm) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    item {
                        PotiDivider(styleType = PotiDividerStyle.SMALL)
                    }
                }
            }
        }
    }
}

private fun Context.openDeepLink(deepLink: String) {
    val intent = Intent(Intent.ACTION_VIEW, deepLink.toUri())
        .setPackage(packageName)

    runCatching { startActivity(intent) }
        .onFailure { Timber.w(it, "Unable to open deep link: $deepLink") }
}

private val previewAlarmSamples = listOf(
    "배송 시작" to "아이브 메이크스타 거래건 배송이 시작되었어요",
    "입금 확인" to "르세라핌 위버스 거래건 입금이 확인되었어요",
    "모집 마감" to "아일릿 앨범 분철 모집이 마감되었어요",
    "새로운 참여자" to "내가 모집한 분철에 새로운 참여자가 들어왔어요",
    "이벤트 도착" to "이번 주 신규 분철 이벤트를 확인해 보세요",
)

private fun previewAlarms(count: Int): ImmutableList<Notification> = List(count) { index ->
    val (title, body) = previewAlarmSamples[index % previewAlarmSamples.size]

    Notification(
        id = index.toLong(),
        title = title,
        body = body,
        type = if (index % previewAlarmSamples.size == 4) NotificationType.EVENT else NotificationType.TRADE,
        deepLink = "",
        isRead = index % 3 != 0,
        createdAt = OffsetDateTime.now(ZoneOffset.UTC).minusMinutes((index + 1) * 47L).toString(),
    )
}.toImmutableList()

@Preview(showBackground = true, name = "알림 없음")
@Composable
private fun AlarmListScreenEmptyPreview() {
    PotiTheme {
        AlarmListScreen(
            uiState = AlarmListUiState(alarmsLoadState = ApiState.Success(persistentListOf())),
            onBackClick = {},
            onSettingClick = {},
            onAlarmClick = {},
            onAlarmReadAllClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true, name = "알림 3개")
@Composable
private fun AlarmListScreenPreview() {
    PotiTheme {
        AlarmListScreen(
            uiState = AlarmListUiState(alarmsLoadState = ApiState.Success(previewAlarms(count = 3))),
            onBackClick = {},
            onSettingClick = {},
            onAlarmClick = {},
            onAlarmReadAllClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true, name = "알림 스크롤")
@Composable
private fun AlarmListScreenScrollablePreview() {
    PotiTheme {
        AlarmListScreen(
            uiState = AlarmListUiState(alarmsLoadState = ApiState.Success(previewAlarms(count = 20))),
            onBackClick = {},
            onSettingClick = {},
            onAlarmClick = {},
            onAlarmReadAllClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
