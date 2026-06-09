package com.poti.android.presentation.history.manage.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.type.ParticipantStatusType
import com.poti.android.presentation.history.manage.model.ParticipantUiModel
import com.poti.android.presentation.history.mapper.color
import com.poti.android.presentation.history.mapper.labelResId
import com.poti.android.presentation.history.mapper.statusColor

@Composable
fun HistoryParticipantDropdown(
    participant: ParticipantUiModel,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    subContent: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PotiTheme.colors.white)
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            ),
    ) {
        ParticipantDropdownHeader(
            name = participant.nickname,
            status = participant.participantStatus,
            expanded = isExpanded,
            onToggle = onToggle,
        )
        AnimatedVisibility(visible = isExpanded) {
            HistoryParticipantDetail(
                participant = participant,
                modifier = Modifier.padding(top = 20.dp),
                content = subContent,
            )
        }
    }
}

@Composable
private fun ParticipantDropdownHeader(
    name: String,
    status: ParticipantStatusType,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = PotiTheme.typography.body16m,
            color = PotiTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        )
        Text(
            text = stringResource(status.labelResId),
            style = PotiTheme.typography.body14sb,
            color = status.statusColor.color,
        )
        Spacer(modifier = Modifier.width(8.dp))

        Crossfade(
            targetState = expanded,
        ) { expand ->
            Icon(
                painter = painterResource(
                    id = if (expand) {
                        R.drawable.ic_arrow_up_lg
                    } else {
                        R.drawable.ic_arrow_down_lg
                    },
                ),
                contentDescription = null,
                tint = PotiTheme.colors.gray700,
            )
        }
    }
}
