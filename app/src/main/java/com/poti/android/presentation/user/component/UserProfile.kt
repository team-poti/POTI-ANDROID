package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun UserProfile(
    imageUrl: String,
    nickname: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(98.dp)
                .clip(CircleShape)
                .background(PotiTheme.colors.gray100),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = nickname,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16sb,
        )

        Text(
            text = email,
            color = PotiTheme.colors.gray700,
            style = PotiTheme.typography.caption12m,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfilePreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UserProfile(
                imageUrl = "",
                nickname = "포티포티포티",
                email = "poti@app.jam",
                modifier = Modifier,
            )
        }
    }
}
