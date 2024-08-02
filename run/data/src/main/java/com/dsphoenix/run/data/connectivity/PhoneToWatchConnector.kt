package com.dsphoenix.run.data.connectivity

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.dsphoenix.core.connectivity.domain.DeviceType
import com.dsphoenix.core.connectivity.domain.NodeDiscovery
import com.dsphoenix.run.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhoneToWatchConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope
): WatchConnector {

    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedDevice = _connectedNode.asStateFlow()

    private val isTrackable = MutableStateFlow(false)

    val messagingActions = nodeDiscovery
        .observeConnectedDevices(DeviceType.PHONE)
        .onEach { connectedDevices ->
            val node = connectedDevices.firstOrNull()
            if (node != null && node.isNearby) {
                _connectedNode.value = node
            }
        }
        .launchIn(applicationScope)

    override fun setIsTrackable(isTrackable: Boolean) {
        this.isTrackable.value = isTrackable
    }
}
