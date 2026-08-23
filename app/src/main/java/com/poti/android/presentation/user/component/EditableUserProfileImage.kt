package com.poti.android.presentation.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.common.extension.noRippleClickable
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun EditableUserProfileImage(
    imageUrl: String?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageSize: Dp = 98.dp,
    editButtonSize: Dp = 36.dp,
    editButtonOverhang: Dp = 7.dp,
) {
    Box(
        modifier = modifier.size(
            width = imageSize + editButtonOverhang * 2,
            height = imageSize,
        ),
        contentAlignment = Alignment.Center,
    ) {
        UserProfileImage(
            imageUrl = imageUrl,
            size = imageSize,
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(editButtonSize)
                .clip(CircleShape)
                .background(PotiTheme.colors.black)
                .noRippleClickable(onEditClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = PotiTheme.colors.white,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditableUserProfileImagePreview() {
    PotiTheme {
        EditableUserProfileImage(
            imageUrl = null,
            onEditClick = {},
        )
    }
}
