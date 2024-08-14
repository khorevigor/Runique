package com.dsphoenix.core.domain.auth

import com.dsphoenix.core.domain.util.Error

enum class AuthError: Error {
    INVALID_CREDENTIALS,
    USER_ALREADY_EXISTS,
    UNKNOWN
}
