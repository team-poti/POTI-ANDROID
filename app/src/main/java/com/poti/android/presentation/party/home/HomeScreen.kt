package com.poti.android.presentation.party.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
) {
    HomeScreen(modifier = modifier)
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "홈",
        modifier = modifier,
    )
}
