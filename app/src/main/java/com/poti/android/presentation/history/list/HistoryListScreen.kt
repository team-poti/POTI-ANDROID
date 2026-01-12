package com.poti.android.presentation.history.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryListRoute(modifier: Modifier = Modifier) {
    HistoryListScreen(modifier = modifier)
}

@Composable
private fun HistoryListScreen(modifier: Modifier = Modifier) {
    Text(
        text = "분철 내역",
        modifier = modifier,
    )
}
