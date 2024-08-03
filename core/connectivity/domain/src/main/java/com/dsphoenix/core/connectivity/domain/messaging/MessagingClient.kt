package com.dsphoenix.core.connectivity.domain.messaging

import com.dsphoenix.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface MessagingClient {
    fun connectToNode(nodeId: String): Flow<MessagingAction>
    suspend fun sendOrQueue(action: MessagingAction): EmptyResult<MessagingError>
}
