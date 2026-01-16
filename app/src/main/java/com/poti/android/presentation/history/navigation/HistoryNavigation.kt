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
import kotlinx.serialization.Serializable

sealed interface HistoryRoute : Route {
    @Serializable
    data object HistoryList : HistoryRoute

    @Serializable
    data object ParticipantDetail : HistoryRoute

    @Serializable
    data object RecruiterDetail : HistoryRoute

    @Serializable
    data object ParticipantManage : HistoryRoute
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

fun NavController.navigateToParticipantManage() {
    navigate(HistoryRoute.ParticipantManage)
}

fun NavGraphBuilder.historyNavGraph(
    paddingValues: PaddingValues,
) {
    composable<HistoryRoute.HistoryList> {
        HistoryListRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<HistoryRoute.ParticipantDetail> {
        ParticipantDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<HistoryRoute.RecruiterDetail> {
        RecruiterDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<HistoryRoute.ParticipantManage> {
        ParticipantManageRoute(modifier = Modifier.padding(paddingValues), onBackClick = {} )
    }
}
