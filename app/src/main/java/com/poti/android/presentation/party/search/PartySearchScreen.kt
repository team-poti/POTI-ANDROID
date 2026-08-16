package com.poti.android.presentation.party.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PartySearchRoute(modifier: Modifier = Modifier) {
}

@Composable
fun PartySearchScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // TODO: 검색 컴포넌트 추가
    }
}

@Preview(showBackground = true)
@Composable
private fun PartySearchScreenPreview() {
    PartySearchScreen()
}
