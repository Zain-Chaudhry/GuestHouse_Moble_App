package com.example.diamondguesthouse.presentation.setting_screen

import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.repo.AuthRepository
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingScreenViewModel(
    private val authRepository: AuthRepository,
) : GenericViewModel<SettingUserEvent, SettingUiEvent>() {

    override fun onUserEvent(event: SettingUserEvent) {
        when (event) {
            SettingUserEvent.SignOutClicked -> {
                authRepository.signOut()
                submitUIEvent(SettingUiEvent.NavigateLogin)
            }
        }
    }
}

sealed interface SettingUserEvent {
    data object SignOutClicked : SettingUserEvent
}

sealed interface SettingUiEvent {
    data object NavigateLogin : SettingUiEvent
}
