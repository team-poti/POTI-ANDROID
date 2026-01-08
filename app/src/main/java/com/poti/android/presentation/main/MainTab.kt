package com.poti.android.presentation.main

import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import com.poti.android.R
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.home.navigation.HomeRoute
import com.poti.android.presentation.mypage.navigation.MyPageRoute
import com.poti.android.presentation.myparty.navigation.MyPartyRoute

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
    ;

    companion object {
        private val tabRouteNames: Set<String> =
            entries.mapNotNull { it.route::class.qualifiedName }.toSet()

        fun contains(destination: NavDestination?): Boolean {
            val r = destination?.route ?: return false
            return r in tabRouteNames
        }
    }
}
