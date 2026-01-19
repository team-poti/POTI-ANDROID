package com.poti.android.presentation.history.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.poti.android.core.designsystem.theme.PotiTheme

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

@Preview(showBackground = true)
@Composable
private fun HistoryListScreenPreview() {
    PotiTheme {
        HistoryListScreen()
    }
}
