package com.poti.android.presentation.myparty

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyPartyListRoute(modifier: Modifier = Modifier) {
    MyPartyListScreen(modifier = modifier)
}

@Composable
private fun MyPartyListScreen(modifier: Modifier = Modifier) {
    Text(
        text = "분철 내역",
    )
}
