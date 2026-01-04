package com.poti.android.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.poti.android.core.base.BaseViewModel
import com.poti.android.core.base.ViewEvent
import com.poti.android.core.base.ViewSideEffect
import com.poti.android.core.base.ViewState

@Composable
fun <S : ViewState, E : ViewEvent, SE : ViewSideEffect> BaseViewModel<S, E, SE>.ObserveSideEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    action: (SE) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(this, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            this@ObserveSideEffect.sideEffect.collect { effect ->
                action(effect)
            }
        }
    }
}
