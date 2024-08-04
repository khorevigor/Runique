package com.dsphoenix.run.presentation.run_overview

import com.dsphoenix.core.domain.run.Run
import com.dsphoenix.presentation.ui.formatted
import com.dsphoenix.presentation.ui.toFormattedHeartRate
import com.dsphoenix.presentation.ui.toFormattedKm
import com.dsphoenix.presentation.ui.toFormattedKmh
import com.dsphoenix.presentation.ui.toFormattedMeters
import com.dsphoenix.presentation.ui.toFormattedPace
import com.dsphoenix.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeLocal = dateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime =
        DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma").format(dateTimeLocal)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate.toFormattedHeartRate(),
        maxHeartRate = maxHeartRate.toFormattedHeartRate()
    )
}
