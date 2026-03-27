package com.example.diamondguesthouse.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class GenericViewModel<UserEvents, UIEvents> : ViewModel() {

    private val _uiEvents: MutableSharedFlow<UIEvents> = MutableSharedFlow(extraBufferCapacity = 64)
    val uiEvents = _uiEvents.asSharedFlow()

    fun submitUIEvent(uiEvents: UIEvents) = viewModelScope.launch(Dispatchers.IO) {
        "onUIEvent:${this@GenericViewModel::class.simpleName} <---------- $uiEvents".toLog()
        _uiEvents.emit(uiEvents)
    }

    fun submitUserEvent(events: UserEvents) {
        "onUserEvent: ${this::class.simpleName} ----------> $events".toLog()
        onUserEvent(events)
    }

    abstract fun onUserEvent(event: UserEvents)

    fun viewModelIOScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.IO, block = block)
}
