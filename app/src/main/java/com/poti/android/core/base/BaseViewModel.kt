package com.poti.android.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ViewState
interface ViewEvent
interface ViewSideEffect

abstract class BaseViewModel<State: ViewState, Event: ViewEvent, SideEffect: ViewSideEffect>(
    initialState: State
): ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    protected val currentState: State
        get() = _uiState.value

    fun setEvent(event: Event) {
        viewModelScope.launch {
            handleEvent(event)
        }
    }

    protected abstract suspend fun handleEvent(event: Event)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.update { currentState.reducer() }
    }

    protected fun postSideEffect(effect: SideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
