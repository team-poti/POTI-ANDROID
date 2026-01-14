package com.poti.android.presentation.party.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage

@Composable
fun PartyDetailRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    PartyDetailScreen(
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun PartyDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.party_detail_title),
        )

        AsyncImage(
            model = "",
            contentDescription = null,
            modifier = Modifier.height(268.dp),
            contentScale = ContentScale.Crop,
        )
    }
}
