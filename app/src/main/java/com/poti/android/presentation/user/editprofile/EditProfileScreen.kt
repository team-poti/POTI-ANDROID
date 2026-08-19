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
fun EditProfileRoute(modifier: Modifier = Modifier) {
}

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PotiHeaderPage(
            onNavigationClick = {},
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
                label = "닉네임",
                placeholder = "닉네임",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen()
}
