package com.dsphoenix.run.data.connectivity

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.dsphoenix.core.connectivity.domain.DeviceType
import com.dsphoenix.core.connectivity.domain.NodeDiscovery
import com.dsphoenix.core.connectivity.domain.messaging.MessagingAction
import com.dsphoenix.core.connectivity.domain.messaging.MessagingClient
import com.dsphoenix.core.connectivity.domain.messaging.MessagingError
import com.dsphoenix.core.domain.util.EmptyResult
import com.dsphoenix.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class PhoneToWatchConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
): WatchConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedDevice = _connectedNode.asStateFlow()

    private val isTrackable = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val messagingActions: Flow<MessagingAction> = nodeDiscovery
        .observeConnectedDevices(localDeviceType = DeviceType.PHONE)
        .flatMapLatest { connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if (node != null && node.isNearby) {
                _connectedNode.value = node
                messagingClient.connectToNode(node.id)
            } else flowOf()
        }
        .onEach { action ->
            if (action == MessagingAction.ConnectionRequest) {
                if (isTrackable.value) {
                    sendToWatch(MessagingAction.Trackable)
                } else {
                    sendToWatch(MessagingAction.Untrackable)
                }
            }
        }
        .shareIn(
            applicationScope,
            SharingStarted.Eagerly
        )

    init {
        _connectedNode
            .filterNotNull()
            .flatMapLatest { isTrackable }
            .onEach { isTrackable ->
                sendToWatch(MessagingAction.ConnectionRequest)
                if (isTrackable) {
                    sendToWatch(MessagingAction.Trackable)
                } else {
                    sendToWatch(MessagingAction.Untrackable)
                }
            }
            .launchIn(applicationScope)
    }

    override suspend fun sendToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueue(action)
    }

    override fun setIsTrackable(isTrackable: Boolean) {
        this.isTrackable.value = isTrackable
    }
}
