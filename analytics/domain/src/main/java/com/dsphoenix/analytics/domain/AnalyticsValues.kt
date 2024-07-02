package com.dsphoenix.analytics.domain

import kotlin.time.Duration

data class AnalyticsValues(
    val totalDistanceMeters: Int = 0,
    val totalTimeMillis: Duration = Duration.ZERO,
    val maxSpeedKmh: Double = 0.0,
    val avgDistanceMeters: Double = 0.0,
    val avgPaceSeconds: Double = 0.0
)
