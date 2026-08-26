package com.poti.android.presentation.party.detail.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.theme.PotiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyShareBottomSheet(
    onDismiss: () -> Unit,
    onLinkCopyClick: () -> Unit,
    onKakaoShareClick: () -> Unit,
    onXShareClick: () -> Unit,
    onSystemShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PotiBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        PartyShareBottomSheetContent(
            onLinkCopyClick = onLinkCopyClick,
            onKakaoShareClick = onKakaoShareClick,
            onXShareClick = onXShareClick,
            onSystemShareClick = onSystemShareClick,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 50.dp),
        )
    }
}

@Composable
fun PartyShareBottomSheetContent(
    onLinkCopyClick: () -> Unit,
    onKakaoShareClick: () -> Unit,
    onXShareClick: () -> Unit,
    onSystemShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PartyShareBottomSheetButton(
            iconRes = R.drawable.ic_link,
            label = stringResource(R.string.party_share_link_copy),
            onClick = onLinkCopyClick,
            iconColor = PotiTheme.colors.black,
        )

        PartyShareBottomSheetButton(
            iconRes = R.drawable.img_kakao_talk,
            label = stringResource(R.string.party_share_kakao_talk),
            innerPadding = PaddingValues(all = 10.dp),
            backgroundColor = Color(0xFFFFE812),
            onClick = onKakaoShareClick,
        )

        PartyShareBottomSheetButton(
            iconRes = R.drawable.ic_x_logo,
            label = stringResource(R.string.party_share_x),
            innerPadding = PaddingValues(all = 0.dp),
            onClick = onXShareClick,
        )

        PartyShareBottomSheetButton(
            iconRes = R.drawable.ic_share,
            label = stringResource(R.string.party_share_system),
            onClick = onSystemShareClick,
            iconColor = PotiTheme.colors.black,
        )
    }
}

@Composable
private fun PartyShareBottomSheetButton(
    @DrawableRes iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(all = 18.dp),
    backgroundColor: Color = PotiTheme.colors.gray100,
    iconColor: Color = Color.Unspecified,
) {
    Column(
        modifier = modifier.noRippleClickable(onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = if (iconColor.isSpecified) ColorFilter.tint(iconColor) else null,
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .background(backgroundColor)
                .padding(innerPadding),
        )

        Text(
            text = label,
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.body14m,
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun PartyShareBottomSheetContentPreview() {
    PotiTheme {
        PartyShareBottomSheetContent(
            onLinkCopyClick = {},
            onKakaoShareClick = {},
            onXShareClick = {},
            onSystemShareClick = {},
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 50.dp),
        )
    }
}
