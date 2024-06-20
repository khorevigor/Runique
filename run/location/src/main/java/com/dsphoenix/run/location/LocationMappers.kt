package com.dsphoenix.run.location

import android.location.Location
import com.dsphoenix.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.dsphoenix.core.domain.location.Location(
            lat = latitude,
            lon = longitude
        ),
        altitude = altitude
    )
}
