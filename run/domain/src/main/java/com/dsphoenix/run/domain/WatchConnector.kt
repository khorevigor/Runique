package com.dsphoenix.run.domain

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.dsphoenix.core.connectivity.domain.messaging.MessagingAction
import com.dsphoenix.core.connectivity.domain.messaging.MessagingError
import com.dsphoenix.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WatchConnector {
    val connectedDevice: StateFlow<DeviceNode?>
    val messagingActions: Flow<MessagingAction>

    suspend fun sendToWatch(action: MessagingAction): EmptyResult<MessagingError>

    fun setIsTrackable(isTrackable: Boolean)
}
