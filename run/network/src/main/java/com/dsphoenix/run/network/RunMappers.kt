package com.dsphoenix.run.network

import com.dsphoenix.core.domain.location.Location
import com.dsphoenix.core.domain.run.Run
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = ZonedDateTime.parse(dateTimeUtc),
        location = Location(lat, long),
        maxSpeedKmh = maxSpeedKmh,
        distanceMeters = distanceMeters,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Run.toRunDto(): RunDto {
    return RunDto(
        id = id,
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeters,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        dateTimeUtc = dateTimeUtc.toString(),
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate,
        mapPictureUrl = mapPictureUrl
    )
}
