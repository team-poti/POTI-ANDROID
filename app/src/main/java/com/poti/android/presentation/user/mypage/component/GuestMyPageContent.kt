package com.poti.android.presentation.user.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.presentation.user.component.BadgeButton

@Composable
fun GuestMyPageContent(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.img_basic_profile),
            contentDescription = null,
            modifier = Modifier
                .size(98.dp)
                .clip(CircleShape),
        )

        Text(
            text = stringResource(R.string.user_guest_description),
            modifier = Modifier.padding(top = 12.dp),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body16sb,
            textAlign = TextAlign.Center,
        )

        BadgeButton(
            bias = stringResource(R.string.user_guest_login),
            onClick = onLoginClick,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFC)
@Composable
private fun GuestMyPageContentPreview() {
    PotiTheme {
        GuestMyPageContent(onLoginClick = {})
    }
}
