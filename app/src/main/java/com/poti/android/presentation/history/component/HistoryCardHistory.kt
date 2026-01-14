package com.poti.android.presentation.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.core.designsystem.theme.PotiTheme.colors
import com.poti.android.core.designsystem.theme.PotiTheme.typography

enum class CardHistorySize(
    val imageSize: Dp,
) {
    SMALL(imageSize = 81.dp),
    LARGE(imageSize = 96.dp),
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
fun HistoryCardHistory(
    sizeType: CardHistorySize,
    imageUrl: String,
    artist: String,
    title: String,
    participantState: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPressed) colors.gray100 else colors.white)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(sizeType.imageSize)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = artist,
                style = sizeType.artistStyle,
                color = colors.gray800,
            )

            Text(
                text = title,
                style = sizeType.titleStyle,
                color = colors.black,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TODO: [천민재] 임시 컴포넌트
            ParticipantState(state = participantState)
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_lg),
            contentDescription = null,
            tint = colors.gray700,
        )
    }
}

// TODO: [천민재] 임시 컴포넌트
@Composable
fun ParticipantState(
    modifier: Modifier = Modifier,
    state: String,
) {
    val (text, color) = when (state) {
        "done" -> "모집 완료" to colors.poti600
        "wait" -> "입금 대기" to colors.sementicRed
        else -> "상태" to colors.gray800
    }

    Text(
        text = text,
        modifier = modifier,
        style = typography.body14sb,
        color = color,
    )
}

@Preview
@Composable
private fun HistoryCardHistoryPreview() {
    var status by remember { mutableStateOf("done") }

    PotiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HistoryCardHistory(
                modifier = Modifier.width(344.dp),
                sizeType = CardHistorySize.LARGE,
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                participantState = status,
                onClick = { status = if (status == "done") "wait" else "done" },
            )

            HistoryCardHistory(
                modifier = Modifier.width(344.dp),
                sizeType = CardHistorySize.SMALL,
                imageUrl = "",
                artist = "ive(아이브)",
                title = "러브다이브 위드뮤",
                participantState = status,
                onClick = { status = if (status == "done") "wait" else "done" },
            )
        }
    }
}
