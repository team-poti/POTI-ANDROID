package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun UserInfo(
    infoList: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.gray100)
            .padding(all = screenWidthDp(12.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        infoList.forEach { info ->
            UserInfoItem(
                infoContent = info,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun UserInfoItem(
    infoContent: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bullet),
            contentDescription = null,
            tint = PotiTheme.colors.gray800,
        )

        Text(
            text = infoContent,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.caption12m,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoCardPreview() {
    PotiTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UserInfo(
                infoList = listOf("최근 3일 이내 활동", "2025년 12월 28일 가입"),
                modifier = Modifier.width(328.dp),
            )
        }
    }
}
