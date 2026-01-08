package com.poti.android.presentation.user.mypage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyPageRoute(modifier: Modifier = Modifier) {
    MyPageScreen(modifier = modifier)
}

@Composable
private fun MyPageScreen(modifier: Modifier = Modifier) {
    Text(
        text = "마이페이지",
        modifier = modifier,
    )
}
