package com.dsphoenix.analytics.presentation

import com.dsphoenix.analytics.domain.AnalyticsValues
import com.dsphoenix.presentation.ui.formatted
import com.dsphoenix.presentation.ui.toFormattedKm
import com.dsphoenix.presentation.ui.toFormattedKmh
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun AnalyticsValues.toAnalyticsDashboardState(): AnalyticsDashboardState =
    AnalyticsDashboardState(
        totalDistance = (totalDistanceMeters / 1000.0).toFormattedKm(),
        totalTime = totalTimeMillis.toFormattedTotalTime(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        avgDistance = (avgDistanceMeters / 1000.0).toFormattedKm(),
        avgPace = avgPaceSeconds.toDuration(DurationUnit.SECONDS).formatted()
    )


private fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60
    return "${days}d ${hours}h ${minutes}m"
}
