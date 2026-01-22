package com.poti.android.presentation.history.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class CardHistorySize {
    SMALL,
    LARGE,
}

val CardHistorySize.artistStyle: TextStyle
    @Composable get() = when (this) {
        CardHistorySize.SMALL -> typography.caption12m
        CardHistorySize.LARGE -> typography.body14m
    }

val CardHistorySize.titleStyle: TextStyle
    @Composable get() = when (this) {
        CardHistorySize.SMALL -> typography.body14m
        CardHistorySize.LARGE -> typography.body16m
    }

@Composable
fun HistoryCardItem(
    sizeType: CardHistorySize,
    imageUrl: String,
    artist: String,
    title: String,
    @StringRes statusTextId: Int,
    statusColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .noRippleClickable(
                interactionSource = interactionSource,
                onClick = onClick,
            )
            .background(if (isPressed) colors.gray100 else colors.white)
            .padding(8.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    vertical =
                        if (sizeType == CardHistorySize.SMALL) 2.dp else 4.dp,
                ),
        ) {
            Text(
                text = artist,
                style = sizeType.artistStyle,
                color = colors.gray800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (sizeType == CardHistorySize.SMALL) {
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = title,
                style = sizeType.titleStyle,
                color = colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(
                modifier = Modifier.height(
                    if (sizeType == CardHistorySize.SMALL) {
                        12.dp
                    } else {
                        22.dp
                    },
                ),
            )

            Text(
                text = stringResource(statusTextId),
                style = typography.body14sb,
                color = statusColor,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_lg),
            contentDescription = null,
            tint = colors.gray700,
        )
    }
}

@Preview
@Composable
private fun HistoryCardItemPreview() {
    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HistoryCardItem(
                modifier = Modifier.width(344.dp),
                sizeType = CardHistorySize.SMALL,
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                statusTextId = R.string.party_status_shipping,
                statusColor = PotiTheme.colors.sementicRed,
                onClick = {},
            )

            HistoryCardItem(
                modifier = Modifier.width(344.dp),
                sizeType = CardHistorySize.LARGE,
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                statusTextId = R.string.party_status_closed,
                statusColor = PotiTheme.colors.poti600,
                onClick = {},
            )
        }
    }
}
