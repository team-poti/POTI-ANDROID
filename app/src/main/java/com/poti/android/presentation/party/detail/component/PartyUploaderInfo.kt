package com.poti.android.presentation.party.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.button.PotiIconButton
import com.poti.android.core.designsystem.component.display.PotiProfileSummary
import com.poti.android.core.designsystem.component.display.PotiProfileSummarySize
import com.poti.android.core.designsystem.theme.PotiTheme
import com.poti.android.domain.model.user.UserSummary
import com.poti.android.presentation.party.detail.dummyPartyDetail

@Composable
fun PartyUploaderInfo(
    userSummary: UserSummary,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(R.string.party_detail_uploader),
            style = PotiTheme.typography.body16sb,
            color = PotiTheme.colors.black,
        )

        Row {
            PotiProfileSummary(
                profileImageUrl = userSummary.profileImage,
                nickname = userSummary.nickname,
                sizeType = PotiProfileSummarySize.LARGE,
                rating = userSummary.rating.toString(),
                reviewText = stringResource(R.string.party_detail_review_count, userSummary.reviewCount),
                modifier = Modifier.weight(1f),
            )

            PotiIconButton(
                iconRes = R.drawable.ic_arrow_right_lg,
                onClick = { onClick(userSummary.userId) },
                tint = PotiTheme.colors.gray700,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PartyUploaderInfoPreview() {
    PotiTheme {
        PartyUploaderInfo(
            userSummary = dummyPartyDetail.userSummary,
            onClick = {},
        )
    }
}
