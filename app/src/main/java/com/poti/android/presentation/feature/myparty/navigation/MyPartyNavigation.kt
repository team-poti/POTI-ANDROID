package com.poti.android.presentation.feature.myparty.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.MyPartyRoute
import com.poti.android.presentation.feature.myparty.MyPartyListRoute
import com.poti.android.presentation.feature.myparty.participant.ParticipantDetailRoute
import com.poti.android.presentation.feature.myparty.recruiter.ParticipantMangeRoute
import com.poti.android.presentation.feature.myparty.recruiter.RecruiterDetailRoute

fun NavGraphBuilder.myPartyNavGraph(
    paddingValues: PaddingValues,
) {
    composable<MyPartyRoute.MyPartyList> {
        MyPartyListRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<MyPartyRoute.ParticipantDetail> {
        ParticipantDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<MyPartyRoute.RecruiterDetail> {
        RecruiterDetailRoute(modifier = Modifier.padding(paddingValues))
    }
    composable<MyPartyRoute.ParticipantManage> {
        ParticipantMangeRoute(modifier = Modifier.padding(paddingValues))
    }
}
