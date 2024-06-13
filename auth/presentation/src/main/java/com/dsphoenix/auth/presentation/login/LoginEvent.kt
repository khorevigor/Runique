package com.dsphoenix.auth.presentation.login

import com.dsphoenix.presentation.ui.UiText

sealed interface LoginEvent {
    data class Error(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}
