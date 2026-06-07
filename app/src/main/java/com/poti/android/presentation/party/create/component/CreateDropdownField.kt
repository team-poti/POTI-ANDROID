package com.poti.android.presentation.party.create.component

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.poti.android.R
import com.poti.android.core.designsystem.component.field.PotiMenuItem
import com.poti.android.core.designsystem.component.field.PotiShortTextField
import com.poti.android.core.designsystem.theme.PotiTheme

enum class ViewType(
    val maxHeight: Dp,
) {
    CREATE_PARTY(156.dp),
    ARTSIT_SELECT(500.dp),
}

@Composable
fun <T> CreateDropdownField(
    viewType: ViewType,
    value: String,
    onValueChanged: (String) -> Unit,
    searchResults: List<T>,
    resultToString: (T) -> String,
    selectedString: String,
    onItemClick: (T) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String = "",
    fieldErrorMsg: String = "",
    showTrailingIcon: Boolean = false,
    readOnly: Boolean = false,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    val scrollState = rememberLazyListState()
    val expandedState = remember { MutableTransitionState(false) }
    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(searchResults.size, isFocused) {
        expandedState.targetState = searchResults.isNotEmpty() && isFocused
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PotiShortTextField(
            value = value,
            onValueChanged = onValueChanged,
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            label = label,
            error = fieldErrorMsg,
            onFocusChanged = {
                isFocused = it
                onFocusChanged?.invoke(it)
            },
            readOnly = readOnly,
            trailingIcon = {
                if (showTrailingIcon) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = PotiTheme.colors.gray700,
                        modifier = Modifier.size(24.dp),
                    )
                }
            },
        )

        AnimatedVisibility(
            visibleState = expandedState,
            enter = slideInVertically(
                initialOffsetY = { -it / 4 },
                animationSpec = tween(120, easing = LinearOutSlowInEasing),
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it / 4 },
                animationSpec = tween(75, easing = LinearOutSlowInEasing),
            ),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, PotiTheme.colors.gray700),
            ) {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.heightIn(max = viewType.maxHeight),
                ) {
                    itemsIndexed(searchResults) { index, result ->
                        val stringResult = resultToString(result)
                        PotiMenuItem(
                            option = stringResult,
                            onClick = {
                                onItemClick(result)

                                expandedState.targetState = false

                                focusManager.clearFocus()
                                keyboardController?.hide()
                            },
                            isSelected = stringResult == selectedString,
                            showBottomBorder = index < searchResults.lastIndex,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateDropdownFieldPrev() {
    var text by remember { mutableStateOf("") }
    val searchResults1 = emptyList<String>()
    val searchResults2 = listOf("옵션1", "옵션2")
    val searchResults3 = listOf("옵션1", "옵션2", "옵션3", "옵션4", "옵션5".repeat(50))

    PotiTheme {
        CreateDropdownField(
            value = text,
            onValueChanged = { text = it },
            searchResults = when (text) {
                "옵션" -> searchResults2
                "옵션2" -> searchResults3
                else -> searchResults1
            },
            onItemClick = { text = it },
            fieldErrorMsg = "에러 메시지",
            placeholder = stringResource(R.string.create_placeholder_product),
            label = stringResource(R.string.create_label_product),
            modifier = Modifier.padding(top = 100.dp),
            resultToString = { text },
            selectedString = "",
            viewType = ViewType.ARTSIT_SELECT,
        )
    }
}
