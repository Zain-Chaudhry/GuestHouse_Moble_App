package com.example.diamondguesthouse.presentation.login_screen

import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

data class LoginUiState(
    val email: String = "",
    val password: String = "",
)

@KoinViewModel
class LoginScreenViewModel(
    private val authRepository: AuthRepository,
) : GenericViewModel<LoginUserEvent, LoginUiEvent>() {

    val authState = authRepository.authState

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    override fun onUserEvent(event: LoginUserEvent) {
        when (event) {
            is LoginUserEvent.EmailChanged -> _uiState.update { it.copy(email = event.value) }
            is LoginUserEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value) }
            is LoginUserEvent.SubmitLogin ->
                authRepository.login(_uiState.value.email, _uiState.value.password)
            is LoginUserEvent.GoogleSignIn -> authRepository.handleGoogleSignIn(event.context)
        }
    }
}

sealed interface LoginUserEvent {
    data class EmailChanged(val value: String) : LoginUserEvent
    data class PasswordChanged(val value: String) : LoginUserEvent
    data object SubmitLogin : LoginUserEvent
    data class GoogleSignIn(val context: android.content.Context) : LoginUserEvent
}

sealed interface LoginUiEvent {
    data object NavigateHome : LoginUiEvent
    data class Toast(val message: String) : LoginUiEvent
}
