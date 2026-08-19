package com.poti.android.presentation.user.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.theme.PotiTheme

/**
 * @param email null이면 이메일을 노출하지 않습니다. 타인의 프로필 조회 시 null을 전달합니다.
 */
@Composable
fun UserProfile(
    nickname: String,
    modifier: Modifier = Modifier,
    email: String? = null,
    imageUrl: String? = "",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserProfileImage(imageUrl = imageUrl)

        Spacer(Modifier.height(12.dp))

        Text(
            text = nickname,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16sb,
        )

        email?.let {
            Spacer(Modifier.height(2.dp))

            Text(
                text = it,
                color = PotiTheme.colors.gray700,
                style = PotiTheme.typography.caption12m,
            )
        }
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
                nickname = "포티포티포티",
                email = "poti@app.jam",
                modifier = Modifier,
            )

            UserProfile(
                nickname = "포티포티포티",
                modifier = Modifier,
            )
        }
    }
}
