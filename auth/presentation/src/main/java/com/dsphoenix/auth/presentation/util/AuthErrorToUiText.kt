package com.dsphoenix.auth.presentation.util

import com.dsphoenix.core.domain.auth.AuthError
import com.dsphoenix.auth.presentation.R
import com.dsphoenix.presentation.ui.UiText

fun AuthError.asUiText(): UiText {
    return when (this) {
        AuthError.INVALID_CREDENTIALS -> UiText.StringResource(R.string.error_email_password_incorrect)
        AuthError.USER_ALREADY_EXISTS -> UiText.StringResource(R.string.error_user_already_exists)
        AuthError.UNKNOWN -> UiText.StringResource(R.string.error_unknown_error)
    }
}
