package com.poti.android.presentation.feature.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.poti.android.core.navigation.MyPageRoute
import com.poti.android.presentation.feature.mypage.MyPageRoute

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
) {
    composable<MyPageRoute.MyPage> {
        MyPageRoute(modifier = Modifier.padding(paddingValues))
    }
}
