package com.poti.android.presentation.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poti.android.R
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.history.navigation.HistoryRoute
import com.poti.android.presentation.party.home.navigation.HomeRoute
import com.poti.android.presentation.user.mypage.navigation.MyPageRoute

enum class MainTab(
    @DrawableRes val iconResId: Int,
    @StringRes val label: Int,
    val route: Route,
) {
    HOME(
        iconResId = R.drawable.ic_home,
        label = R.string.bottom_nav_home,
        route = HomeRoute.Home,
    ),
    MY_PARTY(
        iconResId = R.drawable.ic_history,
        label = R.string.bottom_nav_history,
        route = HistoryRoute.HistoryList,
    ),
    MYPAGE(
        iconResId = R.drawable.ic_mypage,
        label = R.string.bottom_nav_my_page,
        route = MyPageRoute.MyPage,
    ),
}
