package com.example.diamondguesthouse.presentation.sign_up_screen

import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.repo.AuthRepository
class SignUpScreenViewModel(
    private val authRepository: AuthRepository,
) : GenericViewModel<SignUpUserEvent, SignUpUiEvent>() {

    val authState = authRepository.authState

    override fun onUserEvent(event: SignUpUserEvent) {
        when (event) {
            is SignUpUserEvent.Submit -> authRepository.signUp(event.name, event.email, event.password)
            is SignUpUserEvent.GoogleSignIn -> authRepository.handleGoogleSignIn(event.context)
        }
    }
}

sealed interface SignUpUserEvent {
    data class Submit(val name: String, val email: String, val password: String) : SignUpUserEvent
    data class GoogleSignIn(val context: android.content.Context) : SignUpUserEvent
}

sealed interface SignUpUiEvent {
    data object NavigateHome : SignUpUiEvent
    data class Toast(val message: String) : SignUpUiEvent
}
