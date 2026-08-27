package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiInlineButton
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun InquirySection(
    onInquiryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PotiTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.user_inquiry_description),
            modifier = Modifier.fillMaxWidth(),
            color = PotiTheme.colors.gray800,
            style = PotiTheme.typography.body14sb,
            textAlign = TextAlign.Center,
        )

        PotiInlineButton(
            text = stringResource(R.string.user_inquiry_action),
            onClick = onInquiryClick,
            modifier = Modifier.widthIn(min = 97.dp),
            showIcon = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InquirySectionPreview() {
    PotiTheme {
        Column(
            modifier = Modifier
                .background(PotiTheme.colors.gray100)
                .padding(16.dp),
        ) {
            InquirySection(
                onInquiryClick = {},
                modifier = Modifier.width(328.dp),
            )
        }
    }
}
