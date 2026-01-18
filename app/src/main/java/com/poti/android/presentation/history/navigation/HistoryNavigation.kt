package com.poti.android.presentation.history.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.history.list.HistoryListRoute
import com.poti.android.presentation.history.participant.ParticipantDetailRoute
import com.poti.android.presentation.history.recruiter.ParticipantManageRoute
import com.poti.android.presentation.history.recruiter.RecruiterDetailRoute
import com.poti.android.presentation.party.detail.navigation.navigateToPartyDetail
import kotlinx.serialization.Serializable

sealed interface HistoryRoute : Route {
    @Serializable
    data object HistoryList : HistoryRoute

    @Serializable
    data class ParticipantDetail(val recruitId: Long) : HistoryRoute

    @Serializable
    data object RecruiterDetail : HistoryRoute

    @Serializable
    data class ParticipantManage(val recruitId: Long) : HistoryRoute
}

fun NavController.navigateToHistoryList() {
    navigate(HistoryRoute.HistoryList)
}

fun NavController.navigateToParticipantDetail() {
    navigate(HistoryRoute.ParticipantDetail)
}

fun NavController.navigateToRecruiterDetail() {
    navigate(HistoryRoute.RecruiterDetail)
}

fun NavController.navigateToParticipantManage(recruitId: Long) {
    navigate(HistoryRoute.ParticipantManage(recruitId))
}

fun NavGraphBuilder.historyNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<HistoryRoute.HistoryList> {
        HistoryListRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<HistoryRoute.ParticipantDetail> {
        ParticipantDetailRoute(
            modifier = Modifier.padding(paddingValues),
            // TODO: [천민재] pr-58 머지 후, 분철 내역 뷰 연결
            onBackClick = {},
            onNavigateToPartyDetail = navController::navigateToPartyDetail,
        )
    }
    composable<HistoryRoute.RecruiterDetail> {
        RecruiterDetailRoute(
            modifier = Modifier.padding(paddingValues),
            // TODO: [천민재] 마이페이지 모집 내역으로 이동
            onNavigateToMypageRecruit = {},
            // TODO: [천민재] 분철팟 상세 페이지로 이동
            onNavigateToPartyDetail = {},
            onNavigateToParticipantManage = navController::navigateToParticipantManage,
        )
    }
    composable<HistoryRoute.ParticipantManage> {
        ParticipantManageRoute(modifier = Modifier.padding(paddingValues))
    }
}
