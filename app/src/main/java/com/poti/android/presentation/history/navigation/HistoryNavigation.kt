package com.poti.android.presentation.history.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.history.list.HistoryListRoute
import com.poti.android.presentation.history.list.model.HistoryMode
import com.poti.android.presentation.history.manage.ParticipantManageRoute
import com.poti.android.presentation.history.participant.ParticipantDetailRoute
import com.poti.android.presentation.history.recruiter.RecruiterDetailRoute
import com.poti.android.presentation.party.detail.navigation.navigateToPartyDetail
import com.poti.android.presentation.user.component.HistorySummaryType
import kotlinx.serialization.Serializable

sealed interface HistoryRoute : Route {
    @Serializable
    data class HistoryList(
        val mode: HistoryMode? = HistoryMode.RECRUIT,
        val type: HistorySummaryType? = HistorySummaryType.ALL,
    ) : HistoryRoute

    @Serializable
    data class ParticipantDetail(val participantId: Long) : HistoryRoute

    @Serializable
    data class RecruiterDetail(val recruitId: Long) : HistoryRoute

    @Serializable
    data class ParticipantManage(val recruitId: Long) : HistoryRoute
}

fun NavController.navigateToHistoryList(
    mode: HistoryMode,
    type: HistorySummaryType,
) {
    navigate(
        HistoryRoute.HistoryList(
            mode = mode,
            type = type,
        ),
    )
}

fun NavController.navigateToParticipantDetail(participantId: Long) {
    navigate(HistoryRoute.ParticipantDetail(participantId))
}

fun NavController.navigateToRecruiterDetail(recruitId: Long) {
    navigate(HistoryRoute.RecruiterDetail(recruitId))
}

fun NavController.navigateToParticipantManage(recruitId: Long) {
    navigate(HistoryRoute.ParticipantManage(recruitId))
}

fun NavGraphBuilder.historyNavGraph(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    composable<HistoryRoute.HistoryList> {
        HistoryListRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToRecruiterDetail = navController::navigateToRecruiterDetail,
            onNavigateToParticipantDetail = navController::navigateToParticipantDetail,
            modifier = Modifier.padding(paddingValues),
        )
    }
    composable<HistoryRoute.ParticipantDetail> {
        ParticipantDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<HistoryRoute.RecruiterDetail> {
        RecruiterDetailRoute(
            modifier = Modifier.padding(paddingValues),
            onPopBackStack = navController::popBackStack,
            onNavigateToPartyDetail = navController::navigateToPartyDetail,
            onNavigateToParticipantManage = navController::navigateToParticipantManage,
        )
    }
    composable<HistoryRoute.ParticipantManage> {
        ParticipantManageRoute(
            modifier = Modifier.padding(paddingValues),
            popBackStack = navController::popBackStack,
        )
    }
}
