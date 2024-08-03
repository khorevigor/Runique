package com.dsphoenix.wear.run.domain

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.dsphoenix.core.connectivity.domain.messaging.MessagingAction
import com.dsphoenix.core.connectivity.domain.messaging.MessagingError
import com.dsphoenix.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PhoneConnector {
    val connectedNode: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    suspend fun sendToPhone(action: MessagingAction): EmptyResult<MessagingError>
}
