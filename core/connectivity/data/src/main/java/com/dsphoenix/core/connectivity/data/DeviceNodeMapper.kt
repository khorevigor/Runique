package com.dsphoenix.core.connectivity.data

import com.dsphoenix.core.connectivity.domain.DeviceNode
import com.google.android.gms.wearable.Node

fun Node.toDeviceNode(): DeviceNode =
    DeviceNode(
        id = id,
        displayName = displayName,
        isNearby = isNearby
    )
