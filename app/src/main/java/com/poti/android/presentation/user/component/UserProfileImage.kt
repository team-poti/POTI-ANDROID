package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun UserProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 98.dp,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .placeholder(R.drawable.img_basic_profile)
            .error(R.drawable.img_basic_profile)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(PotiTheme.colors.gray100),
    )
}

@Preview(showBackground = true)
@Composable
private fun UserProfileImagePreview() {
    PotiTheme {
        UserProfileImage(imageUrl = null)
    }
}
