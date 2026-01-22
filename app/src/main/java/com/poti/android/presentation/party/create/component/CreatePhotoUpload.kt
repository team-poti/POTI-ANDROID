package com.poti.android.presentation.party.create.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poti.android.R
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.button.DeleteButtonType
import com.poti.android.core.designsystem.component.button.PotiDeleteButton
import com.poti.android.core.designsystem.theme.PotiTheme

private const val MAX_ITEMS = 5

@Composable
fun CreatePhotoUpload(
    imageUris: List<Uri>,
    onImageChanged: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    val remaining = MAX_ITEMS - imageUris.size

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = when {
            remaining <= 1 -> ActivityResultContracts.PickVisualMedia()
            else -> ActivityResultContracts.PickMultipleVisualMedia(remaining.coerceIn(2, 5))
        },
    ) { result ->
        when (result) {
            is Uri -> {
                onImageChanged((imageUris + result).distinct())
            }

            is List<*> -> {
                val uris = result.filterIsInstance<Uri>()
                onImageChanged((imageUris + uris).distinct())
            }
        }
    }

    LazyRow(
        state = lazyListState,
        modifier = modifier
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (imageUris.size < MAX_ITEMS) {
            item {
                UploadButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                .build(),
                        )
                    },
                )
            }
        }

        itemsIndexed(imageUris) { index, uri ->
            PhotoItem(
                image = uri,
                onXClick = {
                    val newUriList = imageUris.filterIndexed { i, _ -> i != index }
                    onImageChanged(newUriList)
                },
            )
        }
    }
}

@Composable
private fun UploadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PotiTheme.colors.gray300)
            .clickable(
                onClick = onClick,
                enabled = enabled,
            )
            .size(screenWidthDp(90.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
            contentDescription = null,
            tint = PotiTheme.colors.gray700,
        )
    }
}

@Composable
private fun PhotoItem(
    image: Uri,
    onXClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(screenWidthDp(90.dp))
            .clip(RoundedCornerShape(8.dp)),
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentScale = ContentScale.Crop,
        )

        PotiDeleteButton(
            onClick = onXClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-8).dp),
            type = DeleteButtonType.LIGHT,
        )
    }
}

@Preview
@Composable
private fun CreatePhotoUploadPreview() {
    var imageUris by remember { mutableStateOf<List<Uri>>((emptyList())) }

    PotiTheme {
        CreatePhotoUpload(
            imageUris = imageUris,
            onImageChanged = { imageUris = it },
            modifier = Modifier
                .padding(top = 100.dp),
        )
    }
}
