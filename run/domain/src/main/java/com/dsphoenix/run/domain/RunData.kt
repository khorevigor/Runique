package com.dsphoenix.run.domain

import com.dsphoenix.core.domain.location.LocationTimestamp
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<LocationTimestamp> = emptyList()
)
