package com.poti.android.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route

sealed interface AuthRoute : Route {
    @Serializable
    data object Login : AuthRoute
}

sealed interface OnboardingRoute : Route {
    @Serializable
    data object Guide : OnboardingRoute

    @Serializable
    data object Nickname : OnboardingRoute

    @Serializable
    data object Artist : OnboardingRoute
}

sealed interface HomeRoute : Route {
    @Serializable
    data object Home : HomeRoute
}

sealed interface MyPageRoute : Route {
    @Serializable
    data object MyPage : MyPageRoute
}

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

sealed interface GoodsRoute : Route {
    @Serializable
    data object GoodsList : GoodsRoute

    @Serializable
    data object GoodsPartyList : GoodsRoute
}

sealed interface PartyCreateRoute : Route {
    @Serializable
    data object PartyCreate : PartyCreateRoute

    @Serializable
    data object PartyArtistSelect : PartyCreateRoute
}

sealed interface PartyDetailRoute : Route {
    @Serializable
    data object PartyDetail : PartyDetailRoute

    @Serializable
    data object PartyJoin : PartyDetailRoute
}

sealed interface ProfileRoute : Route {
    @Serializable
    data object Profile : ProfileRoute
}
