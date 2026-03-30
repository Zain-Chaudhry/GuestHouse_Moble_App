package com.example.diamondguesthouse.presentation.forgot_password_screen

import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.repo.AuthRepository
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ForgotPasswordScreenViewModel(
    private val authRepository: AuthRepository,
) : GenericViewModel<ForgotPasswordUserEvent, ForgotPasswordUiEvent>() {

    val authState = authRepository.authState

    override fun onUserEvent(event: ForgotPasswordUserEvent) {
        when (event) {
            is ForgotPasswordUserEvent.Submit -> authRepository.resetPassword(event.email)
        }
    }
}

sealed interface ForgotPasswordUserEvent {
    data class Submit(val email: String) : ForgotPasswordUserEvent
}

sealed interface ForgotPasswordUiEvent {
    data class Toast(val message: String) : ForgotPasswordUiEvent
}
