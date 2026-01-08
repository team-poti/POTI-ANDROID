package com.poti.android.presentation.main

import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.myparty.navigation.MyPartyRoute
import com.poti.android.presentation.party.home.navigation.HomeRoute
import com.poti.android.presentation.user.mypage.navigation.MyPageRoute

enum class MainTab(
    @StringRes val label: Int,
    val route: Route,
) {
    HOME(
        label = R.string.bottom_nav_home,
        route = HomeRoute.Home,
    ),
    MY_PARTY(
        label = R.string.bottom_nav_my_party,
        route = MyPartyRoute.MyPartyList,
    ),
    MY_PAGE(
        label = R.string.bottom_nav_my_page,
        route = MyPageRoute.MyPage,
    ),
}
