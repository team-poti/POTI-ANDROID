package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UserInfo(
    activityMessage: String,
    joinedAt: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserInfoText(text = activityMessage)

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bullet),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = PotiTheme.colors.gray800,
        )

        UserInfoText(text = formatJoinedDate(joinedAt))
    }
}

@Composable
private fun UserInfoText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = PotiTheme.colors.gray800,
        style = PotiTheme.typography.body14m,
    )
}

private fun formatJoinedDate(joinedAt: String): String {
    return try {
        val date = LocalDate.parse(joinedAt)
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
        "${date.format(formatter)} 가입"
    } catch (e: Exception) {
        joinedAt
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier
                .background(PotiTheme.colors.gray100)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UserInfo(
                activityMessage = "최근 3일 이내 활동",
                joinedAt = "2025-12-28",
                modifier = Modifier.width(328.dp),
            )
        }
    }
}
