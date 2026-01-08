package com.poti.android.presentation.party.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poti.android.core.common.extension.noRippleClickable

@Composable
fun HomeRoute(
    onNavigateToGoodsCategory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeScreen(
        onClick = onNavigateToGoodsCategory,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "홈",
        modifier = modifier.noRippleClickable(onClick),
    )
}
