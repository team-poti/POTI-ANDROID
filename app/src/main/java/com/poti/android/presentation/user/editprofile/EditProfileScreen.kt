package com.poti.android.presentation.user.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poti.android.R
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.component.navigation.PotiHeaderPage
import com.poti.android.presentation.user.component.EditableUserProfileImage

@Composable
fun EditProfileRoute(
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EditProfileScreen(
        onBackClick = onPopBackStack,
        modifier = modifier,
    )
}

@Composable
private fun EditProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = onBackClick,
            title = stringResource(R.string.profile_management_title),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            EditableUserProfileImage(
                imageUrl = null,
                onEditClick = {},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 24.dp),
            )

            PotiShortTextField(
                value = "",
                onValueChanged = {},
                label = stringResource(R.string.profile_nickname),
                placeholder = stringResource(R.string.profile_nickname),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(
        onBackClick = {},
    )
}
