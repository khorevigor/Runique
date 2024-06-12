package com.dsphoenix.auth.presentation.register

import com.dsphoenix.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}
