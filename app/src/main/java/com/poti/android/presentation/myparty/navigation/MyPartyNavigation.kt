package com.poti.android.presentation.myparty.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.myparty.list.MyPartyListRoute
import com.poti.android.presentation.myparty.participant.ParticipantDetailRoute
import com.poti.android.presentation.myparty.recruiter.ParticipantMangeRoute
import com.poti.android.presentation.myparty.recruiter.RecruiterDetailRoute
import kotlinx.serialization.Serializable

sealed interface MyPartyRoute : Route {
    @Serializable
    data object MyPartyList : MyPartyRoute

    @Serializable
    data object ParticipantDetail : MyPartyRoute

    @Serializable
    data object RecruiterDetail : MyPartyRoute

    @Serializable
    data object ParticipantManage : MyPartyRoute
}

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
