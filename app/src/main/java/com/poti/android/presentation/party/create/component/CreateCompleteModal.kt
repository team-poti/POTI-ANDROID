package com.poti.android.presentation.party.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.core.designsystem.component.modal.PotiModal
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.component.button.ModalButtonType
import com.poti.android.core.designsystem.component.button.PotiModalButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun CreateCompleteModal(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PotiModal(
        onDismissRequest = onDismiss,
        modifier = modifier.padding(horizontal = 28.dp),
    ) {
        CreateCompleteModalContent(
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}

@Composable
private fun CreateCompleteModalContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        CreateCompleteModalHeader(onDismiss = onDismiss)

        Spacer(Modifier.height(5.dp))

        Text(
            text = stringResource(R.string.create_complete_modal_subtitle),
            color = PotiTheme.colors.poti800,
            style = PotiTheme.typography.body14m,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(
                    color = PotiTheme.colors.gray100,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(vertical = 7.5.dp)
        )

        Spacer(Modifier.height(12.dp))

        CreateCompleteModalNoticeList(
            notices = rememberCreateCompleteModalNotices(),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.create_complete_modal_agreement),
            color = PotiTheme.colors.gray900,
            style = PotiTheme.typography.body14m,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(
                    color = PotiTheme.colors.gray100,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(12.dp))

        PotiModalButton(
            text = stringResource(R.string.create_complete_modal_confirm),
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            type = ModalButtonType.SECONDARY,
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CreateCompleteModalHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 4.dp, top = 4.dp),
    ) {
        Text(
            text = stringResource(R.string.create_notice_title),
            color = PotiTheme.colors.black,
            style = PotiTheme.typography.title18sb,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_x),
            contentDescription = null,
            tint = PotiTheme.colors.black,
            modifier = Modifier
                .noRippleClickable(onClick = onDismiss)
                .padding(all = 12.dp),
        )
    }
}

@Composable
private fun rememberCreateCompleteModalNotices(): ImmutableList<String> {
    val noticesArray = stringArrayResource(R.array.create_complete_modal_notices)
    return remember(noticesArray) { noticesArray.toPersistentList() }
}

@Composable
private fun CreateCompleteModalNoticeList(
    notices: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .heightIn(max = 280.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        notices.forEach { notice ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bullet),
                    contentDescription = null,
                    tint = PotiTheme.colors.gray900,
                )

                Text(
                    text = notice,
                    color = PotiTheme.colors.gray900,
                    style = PotiTheme.typography.body14m,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateCompleteModalContentPreview() {
    PotiTheme {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PotiTheme.colors.white,
        ) {
            CreateCompleteModalContent(
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
