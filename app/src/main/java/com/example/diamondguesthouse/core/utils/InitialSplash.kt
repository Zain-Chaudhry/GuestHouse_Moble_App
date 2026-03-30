package com.example.diamondguesthouse.core.utils

import org.koin.core.annotation.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Single
class InitialSplash {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        scope.launch {
            delay(2000L)
            _isReady.value = true
        }
    }
}
