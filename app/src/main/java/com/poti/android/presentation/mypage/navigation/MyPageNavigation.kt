package com.poti.android.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.Route
import com.poti.android.presentation.mypage.MyPageRoute
import kotlinx.serialization.Serializable

sealed interface MyPageRoute : Route {
    @Serializable
    data object MyPage : MyPageRoute
}

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
) {
    composable<MyPageRoute.MyPage> {
        MyPageRoute(modifier = Modifier.padding(paddingValues))
    }
}
