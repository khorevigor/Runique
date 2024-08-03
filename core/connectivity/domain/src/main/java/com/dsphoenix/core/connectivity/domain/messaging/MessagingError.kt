package com.dsphoenix.core.connectivity.domain.messaging

import com.dsphoenix.core.domain.util.Error

enum class MessagingError: Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}
