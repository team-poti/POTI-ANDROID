package com.poti.android.presentation.party.create.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.DeleteButtonType
import com.poti.android.core.designsystem.component.button.PotiDeleteButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PhotoItem(
    image: Uri,
    onXClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(screenWidthDp(90.dp))
            .clip(RoundedCornerShape(8.dp)),
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentScale = ContentScale.Crop,
        )

        PotiDeleteButton(
            onClick = onXClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-8).dp),
            type = DeleteButtonType.LIGHT,
        )
    }
}

@Preview
@Composable
private fun PhotoItemPreview() {
    PotiTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PhotoItem(
                image = Uri.EMPTY,
                onXClick = {},
            )
        }
    }
}
