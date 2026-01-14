package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun PartyDetailHeaderInfo(
    artist: String,
    goodsName: String,
    price: String,
    time: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = artist,
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = goodsName,
                style = PotiTheme.typography.title18sb,
                color = PotiTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
            ) {
                Text(
                    text = price,
                    style = PotiTheme.typography.display20b,
                    color = PotiTheme.colors.black,
                )
                Text(
                    text = stringResource(R.string.party_detail_price_per_person),
                    style = PotiTheme.typography.body16m,
                    color = PotiTheme.colors.gray800,
                )
            }

            Text(
                text = time,
                style = PotiTheme.typography.body14m,
                color = PotiTheme.colors.gray800,
            )
        }

        PotiIconButton(
            iconRes = R.drawable.ic_heart,
            onClick = onFavoriteClick,
            // TODO: [지현] 색 연결하기
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyDetailHeaderInfoPreview() {
    PotiTheme {
        PartyDetailHeaderInfo(
            artist = "IVE(아이브)",
            goodsName = "러브다이브 위드뮤",
            price = "5,000원~",
            time = "4시간 전",
            isFavorite = false,
            onFavoriteClick = {},
        )
    }
}
