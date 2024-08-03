package com.dsphoenix.wear.run.data

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.dsphoenix.core.connectivity.domain.DeviceType
import com.dsphoenix.core.connectivity.domain.NodeDiscovery
import com.dsphoenix.core.connectivity.domain.messaging.MessagingAction
import com.dsphoenix.core.connectivity.domain.messaging.MessagingClient
import com.dsphoenix.core.connectivity.domain.messaging.MessagingError
import com.dsphoenix.core.domain.util.EmptyResult
import com.dsphoenix.wear.run.domain.PhoneConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

class WatchToPhoneConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
) : PhoneConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedNode = _connectedNode.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val messagingActions = nodeDiscovery
        .observeConnectedDevices(DeviceType.WATCH)
        .flatMapLatest { connectedNodes ->
            val node = connectedNodes.firstOrNull()
            if (node != null && node.isNearby) {
                _connectedNode.value = node
                messagingClient.connectToNode(node.id)
            } else flowOf()
        }
        .shareIn(
            applicationScope,
            SharingStarted.Eagerly
        )

    override suspend fun sendToPhone(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueue(action)
    }
}
