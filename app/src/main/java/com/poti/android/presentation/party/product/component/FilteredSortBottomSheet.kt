package com.poti.android.presentation.party.product.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poti.android.core.common.util.screenWidthDp
import com.poti.android.core.designsystem.component.bottomsheet.PotiBottomSheet
import com.poti.android.core.designsystem.component.display.PotiListRadio
import com.poti.android.presentation.party.product.partylist.model.PartySortType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilteredSortBottomSheet(
    selectedSortType: PartySortType,
    onSelect: (PartySortType) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortTypes = remember { PartySortType.entries.toList() }

    val options = remember {
        sortTypes.map { it.displayRes }.toImmutableList()
    }

    val selectedIndex = sortTypes.indexOf(selectedSortType)
    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    PotiBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.padding(),
    ) {
        PotiListRadio(
            options = options.map { stringResource(it) }.toImmutableList(),
            selectedOptionIndex = selectedIndex,
            onClick = { index ->
                onSelect(sortTypes[index])
                scope.launch {
                    sheetState.hide()
                }
            },
            modifier = Modifier
                .padding(horizontal = screenWidthDp(16.dp))
                .padding(top = 16.dp, bottom = 48.dp),
        )
    }
}
